package cn.com.gpic.ini.file.domain.dto;

import cn.com.gpic.ini.common.util.wrapper.condition.annotations.SqlCondition;
import cn.com.gpic.ini.common.util.wrapper.condition.domain.SqlSymbol;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("文件查询参数")
@Data
public class FileInfoQueryParam {

    @ApiModelProperty("文件名")
    @SqlCondition(SqlSymbol.LIKE)
    private String name;

    @ApiModelProperty("媒体类型")
    @SqlCondition(SqlSymbol.LIKE)
    private String mimeType;

    @ApiModelProperty("大小")
    private String fileSize;

    @ApiModelProperty("路径")
    @SqlCondition(SqlSymbol.LIKE)
    private String path;

    @ApiModelProperty("版本关联主键")
    private String versionId;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("缩略图主键")
    private String thumbnailId;

    @ApiModelProperty("是否临时文件")
    private String isTemp;

    @ApiModelProperty("是否加密存储")
    private String isEncrypt;

    @ApiModelProperty("是否压缩存储")
    private String isCompress;

    @ApiModelProperty("SHA256特征值")
    private String sha256sum;


}
