package io.metersphere.base.mapper.ext;

import io.metersphere.api.dto.datacount.ApiDataCountResult;
import io.metersphere.api.dto.definition.ApiComputeResult;
import io.metersphere.api.dto.definition.ApiDefinitionRequest;
import io.metersphere.api.dto.definition.ApiDefinitionResult;
import io.metersphere.api.dto.definition.ApiSwaggerUrlDTO;
import io.metersphere.base.domain.ApiDefinition;
import io.metersphere.base.domain.ApiDefinitionExample;
import io.metersphere.controller.request.BaseQueryRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ExtApiDefinitionMapper {
    List<ApiSwaggerUrlDTO> selectScheduleList(@Param("projectId") String projectId);

    List<ApiDefinitionResult> list(@Param("request") ApiDefinitionRequest request);

    int moduleCount(@Param("request") ApiDefinitionRequest request);

    //List<ApiComputeResult> selectByIds(@Param("ids") List<String> ids);

    List<ApiComputeResult> selectByIds(@Param("ids") List<String> ids, @Param("projectId") String projectId);

    int removeToGc(@Param("ids") List<String> ids);

    int removeToGcByExample(ApiDefinitionExample example);

    int reduction(@Param("ids") List<String> ids);

    List<ApiDataCountResult> countProtocolByProjectID(String projectId);

    Long countByProjectIDAndCreateInThisWeek(@Param("projectId") String projectId, @Param("firstDayTimestamp") long firstDayTimestamp, @Param("lastDayTimestamp") long lastDayTimestamp);

    Long countByProjectIDAndCreateInThisWeekNoP4(@Param("projectId") String projectId, @Param("firstDayTimestamp") long firstDayTimestamp, @Param("lastDayTimestamp") long lastDayTimestamp);

    Long countByProjectIDAndTagAndCreateInThisWeek(@Param("projectId") String projectId, @Param("tag") String tag, @Param("firstDayTimestamp") long firstDayTimestamp, @Param("lastDayTimestamp") long lastDayTimestamp);

    List<ApiDataCountResult> countStateByProjectID(String projectId);

    List<ApiDataCountResult> countStateByProjectIDP0(String projectId);

    List<ApiDataCountResult> countStateByProjectIDNoP4(String projectId);

    List<ApiDataCountResult> countApiCoverageByProjectID(String projectId);

    ApiDefinition getNextNum(@Param("projectId") String projectId);

    List<ApiDefinitionResult> listRelevance(@Param("request") ApiDefinitionRequest request);

    List<ApiDefinitionResult> listRelevanceReview(@Param("request") ApiDefinitionRequest request);

    List<String> selectIds(@Param("request") BaseQueryRequest query);

    List<ApiDefinition> selectEffectiveIdByProjectId(String projectId);

    List<ApiDefinitionResult> listByIds(@Param("ids") List<String> ids);

    List<Map<String, Object>> moduleCountByCollection(@Param("request") ApiDefinitionRequest request);
}
