package com.qingming.jobserver.controller;

import com.qingming.jobserver.common.BaseResponse;
import com.qingming.jobserver.common.ResultUtils;
import com.qingming.jobserver.model.dao.occupation.AddOccupationDao;
import com.qingming.jobserver.model.dao.occupation.UpdateOccupationDao;
import com.qingming.jobserver.model.entity.Occupation;
import com.qingming.jobserver.service.OccupationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 工种管理控制器
 */
@RestController
@RequestMapping("/occupation")
public class OccupationController {

    @Autowired
    private OccupationService occupationService;

    /**
     * 添加工种
     * @param addOccupationDao 添加工种请求对象
     * @return 添加后的工种对象
     */
    @PostMapping("/add")
    public BaseResponse<Occupation> addOccupation(@RequestBody @Valid AddOccupationDao addOccupationDao) {
        Occupation occupation = occupationService.addOccupation(addOccupationDao);
        return ResultUtils.success(occupation);
    }

    /**
     * 更新工种信息
     * @param updateOccupationDao 更新工种请求对象
     * @return 更新后的工种对象
     */
    @PutMapping("/update")
    public BaseResponse<Occupation> updateOccupation(@RequestBody @Valid UpdateOccupationDao updateOccupationDao) {
        Occupation occupation = occupationService.updateOccupation(updateOccupationDao);
        return ResultUtils.success(occupation);
    }

    /**
     * 根据ID获取工种详情
     * @param id 工种ID
     * @return 工种对象
     */
    @GetMapping("/info/{id}")
    public BaseResponse<Occupation> getOccupationById(@PathVariable Integer id) {
        Occupation occupation = occupationService.getOccupationById(id);
        return ResultUtils.success(occupation);
    }

    /**
     * 获取工种列表，可选择按类别过滤
     * @param category 工种类别（可选）
     * @return 工种列表
     */
    @GetMapping("/list")
    public BaseResponse<List<Occupation>> listOccupations(@RequestParam(required = false) String category) {
        List<Occupation> occupations;
        if (StringUtils.hasText(category)) {
            occupations = occupationService.listOccupationsByCategory(category);
        } else {
            occupations = occupationService.listOccupations();
        }
        return ResultUtils.success(occupations);
    }

    /**
     * 删除工种
     * @param id 工种ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public BaseResponse<String> deleteOccupation(@PathVariable Integer id) {
        boolean result = occupationService.deleteOccupation(id);
        if (result) {
            return ResultUtils.success("删除成功");
        } else {
            return ResultUtils.error(50001, "删除失败");
        }
    }
}