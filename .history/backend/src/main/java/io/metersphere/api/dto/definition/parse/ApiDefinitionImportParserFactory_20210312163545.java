package io.metersphere.api.dto.definition.parse;

import io.metersphere.api.parse.ApiImportParser;
import io.metersphere.commons.constants.ApiImportPlatform;
import org.apache.commons.lang3.StringUtils;

public class ApiDefinitionImportParserFactory {
    public static ApiImportParser getApiImportParser(String platform) {
        if (StringUtils.equals(ApiImportPlatform.Metersphere.name(), platform)) {
            return new MsDefinitionParser();
        } else if (StringUtils.equals(ApiImportPlatform.Postman.name(), platform)) {
            return new PostmanDefinitionParser();
        } else if (StringUtils.equals(ApiImportPlatform.Swagger2.name(), platform)) {
            return new Swagger2Parser();
<<<<<<< HEAD
        } else if (StringUtils.equals(ApiImportPlatform.Forseti.name(), platform)) {
            return new ForsetiParser();
=======
        }else if (StringUtils.equals(ApiImportPlatform.Har.name(), platform)) {
            return new HarParser();
>>>>>>> master
        }
        return null;
    }
}