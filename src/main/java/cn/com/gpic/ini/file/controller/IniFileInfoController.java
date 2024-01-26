package cn.com.gpic.ini.file.controller;

import cn.com.gpic.ini.common.domain.PageQuery;
import cn.com.gpic.ini.common.util.wrapper.WrapperUtils;
import cn.com.gpic.ini.file.domain.dto.FileInfoQueryParam;
import cn.com.gpic.ini.file.entity.IniFileInfo;
import cn.com.gpic.ini.file.service.IniFileInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.vampireachao.stream.plugin.mybatisplus.Database;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 文件 前端控制器
 * </p>
 *
 * @author Gpic
 * @since 2022-10-20 12:11:24
 */
@Api(tags = "文件管理接口")
@RestController
@RequestMapping("/${base.web}/fileInfo")
public class IniFileInfoController {

    @Resource
    private IniFileInfoService service;

    @ApiOperation("根据条件查询文件分页列表")
    @GetMapping("/list")
    public IPage<IniFileInfo> listByParams(@ModelAttribute FileInfoQueryParam param, PageQuery pageQuery) {
        return Database.page(pageQuery.build(), WrapperUtils.query(param, IniFileInfo.class));
    }

    @ApiOperation("根据ids查询文件列表")
    @GetMapping("/ids")
    public List<IniFileInfo> listById(@RequestParam Set<String> ids) {
        return Database.listByIds(ids, IniFileInfo.class);
    }

    @ApiOperation("根据id查询")
    @GetMapping("/{id}")
    public IniFileInfo get(@PathVariable("id") String id) {
        return Database.getById(id, IniFileInfo.class);
    }

    @ApiOperation("预览")
    @GetMapping("/preview")
    public void preview(@RequestParam String id, @RequestParam(required = false) Boolean thumbnails, HttpServletResponse response) {
        service.preview(response, BooleanUtils.isTrue(thumbnails), id);
    }

    @ApiOperation("下载")
    @GetMapping("/download")
    public void download(@RequestParam String id, @RequestParam(required = false) String fileName, @RequestParam(required = false) Boolean thumbnails, HttpServletRequest request, HttpServletResponse response) {
        service.download(request, response, fileName, BooleanUtils.isTrue(thumbnails), id);
    }

    @ApiOperation("批量下载")
    @GetMapping({"/download/batch"})
    public void batchDownload(@RequestParam String[] ids, @RequestParam(required = false) String fileName, @RequestParam(required = false) Boolean thumbnails, HttpServletRequest request, HttpServletResponse response) {
        service.download(request, response, fileName, BooleanUtils.isTrue(thumbnails), ids);
    }

    @ApiOperation("上传")
    @PostMapping({"/upload"})
    public IniFileInfo upload(@RequestPart MultipartFile file) {
        return service.saveFile(file);
    }

    @ApiOperation("批量上传")
    @PostMapping({"/upload/batch"})
    public List<IniFileInfo> batchUpload(@RequestPart MultipartFile[] files) {
        return service.saveFile(files);
    }

    @ApiOperation("删除")
    @DeleteMapping({"{id}"})
    public void remove(@PathVariable String id) {
        service.deleteFile(id);
    }


}
