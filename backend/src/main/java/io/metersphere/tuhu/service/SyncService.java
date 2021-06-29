package io.metersphere.tuhu.service;

import com.alibaba.fastjson.JSONObject;
import io.metersphere.tuhu.dto.OrgAndWsInfoDTO;
import io.metersphere.tuhu.dto.OrganizationDTO;
import io.metersphere.tuhu.dto.WorkSpaceDTO;
import io.metersphere.tuhu.mapper.TuhuAllAppIdMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;


@Service
@Transactional(rollbackFor = Exception.class)
public class SyncService {
    private static Map<String, OrganizationDTO> org = new HashMap<>();

    @Resource
    private TuhuAllAppIdMapper tuhuAllAppIdMapper;

    public void convertItem(JSONObject item) {
        String teamPath = item.getString("teamPath");
        if (StringUtils.isBlank(teamPath)) {
            return;
        }
        String appId = item.getString("appid");
        String[] orgArray = teamPath.split(">");
        String orgName = "";
        String wsName = "";

        if (orgArray.length == 3) {
            orgName = orgArray[0];  // 二级部门
            wsName = orgArray[2];   // 四级部门
        } else if (orgArray.length == 2) {
            orgName = orgArray[0];  // 二级部门
            wsName = orgArray[1];   // 三级部门
        } else {
            return;
        }
        // 组织不存在创建组织、工作空间、添加appId
        if (!org.containsKey(orgName)) {
            OrganizationDTO organizationDTO = new OrganizationDTO();
            Map<String, WorkSpaceDTO> wsMap = new HashMap<>();
            WorkSpaceDTO workSpaceDTO = new WorkSpaceDTO();
            List<String> appIds = new ArrayList<>();

            workSpaceDTO.setName(wsName);
            appIds.add(appId);
            workSpaceDTO.setAppIdList(appIds);
            wsMap.put(wsName, workSpaceDTO);

            organizationDTO.setName(orgName);
            organizationDTO.setWorkSpace(wsMap);
            org.put(orgName, organizationDTO);
        } else {
            OrganizationDTO organizationDTO = org.get(orgName);
            Map<String, WorkSpaceDTO> wsMap = organizationDTO.getWorkSpace();
            // 工作空间不存在 创建工作空间、添加appId
            if (!wsMap.containsKey(wsName)) {
                WorkSpaceDTO workSpaceDTO = new WorkSpaceDTO();
                List<String> appIds = new ArrayList<>();

                workSpaceDTO.setName(wsName);
                appIds.add(appId);
                workSpaceDTO.setAppIdList(appIds);
                wsMap.put(wsName, workSpaceDTO);
            } else {
                WorkSpaceDTO workSpaceDTO = wsMap.get(wsName);
                workSpaceDTO.getAppIdList().add(appId);
            }
        }
    }

    public void updateAppId(TuhuAllAppIdMapper tuhuAllAppIdMapper) {
        org.forEach((orgName, orgDTO) -> {
            Map<String, WorkSpaceDTO> ws = orgDTO.getWorkSpace();
            ws.forEach((wsName, wsDTO) -> {
                List<String> appIds = wsDTO.getAppIdList();
                OrgAndWsInfoDTO orgAndWsInfoDTO = tuhuAllAppIdMapper.queryOrgAndWsInfoByName(orgName, wsName);
                if (orgAndWsInfoDTO != null) {
                    List<OrgAndWsInfoDTO> list = new ArrayList<>();
                    appIds.forEach(item -> {
                        OrgAndWsInfoDTO temp = new OrgAndWsInfoDTO();
                        temp.setId(UUID.randomUUID().toString());
                        temp.setAppId(item);
                        temp.setOrgId(orgAndWsInfoDTO.getOrgId());
                        temp.setWsId(orgAndWsInfoDTO.getWsId());
                        list.add(temp);
                    });

                    tuhuAllAppIdMapper.deleteByOrgIdAndWsId(orgAndWsInfoDTO);
                    tuhuAllAppIdMapper.addByOrgIdAndWsId(list);
                }
            });
        });
    }

}
