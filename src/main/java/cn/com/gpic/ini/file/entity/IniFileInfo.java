package cn.com.gpic.ini.file.entity;

import cn.com.gpic.ini.file.constant.FileConstants;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文件
 * </p>
 *
 * @author lzk&yjj
 * @since 2022-10-20 12:11:24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(FileConstants.TABLE_FILE)
@ApiModel(value = "IniFileInfo对象", description = "文件")
public class IniFileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "ID", type = IdType.NONE)
    private String id;

    @ApiModelProperty("文件名")
    @TableField("NAME")
    private String name;

    @ApiModelProperty("媒体类型")
    @TableField("MIME_TYPE")
    private String mimeType;

    @ApiModelProperty("大小")
    @TableField("FILE_SIZE")
    private String fileSize;

    @ApiModelProperty("路径")
    @TableField("PATH")
    private String path;

    @ApiModelProperty("版本关联主键")
    @TableField("VERSION_ID")
    private String versionId;

    @ApiModelProperty("版本号")
    @TableField("VERSION_NO")
    private String versionNo;

    @ApiModelProperty("缩略图主键")
    @TableField("THUMBNAIL_ID")
    private String thumbnailId;

    @ApiModelProperty("是否临时文件")
    @TableField("IS_TEMP")
    private String isTemp;

    @ApiModelProperty("是否加密存储")
    @TableField("IS_ENCRYPT")
    private String isEncrypt;

    @ApiModelProperty("是否压缩存储")
    @TableField("IS_COMPRESS")
    private String isCompress;

    @ApiModelProperty("SHA256特征值")
    @TableField("SHA256SUM")
    private String sha256sum;

    @ApiModelProperty(value = "创建人")
    @TableField(value = "CREATED_BY", fill = FieldFill.INSERT)
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
    private Date createdTime;

    @ApiModelProperty(value = "更新人")
    @TableField(value = "UPDATED_BY", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "UPDATED_TIME", fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;


}
