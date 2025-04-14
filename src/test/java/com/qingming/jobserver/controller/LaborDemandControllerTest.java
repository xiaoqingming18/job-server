package com.qingming.jobserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingming.jobserver.common.BaseResponse;
import com.qingming.jobserver.common.CurrentUserUtils;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandAddDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandQueryDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandStatusUpdateDao;
import com.qingming.jobserver.model.dao.labordemand.LaborDemandUpdateDao;
import com.qingming.jobserver.model.vo.LaborDemandVO;
import com.qingming.jobserver.service.LaborDemandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LaborDemandControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LaborDemandService laborDemandService;

    @InjectMocks
    private LaborDemandController laborDemandController;

    private LaborDemandVO laborDemandVO;
    private LaborDemandAddDao laborDemandAddDao;
    private LaborDemandUpdateDao laborDemandUpdateDao;
    private LaborDemandStatusUpdateDao statusUpdateDao;
    private String laborDemandAddDaoJson;
    private String laborDemandUpdateDaoJson;
    private String statusUpdateDaoJson;
    private String laborDemandQueryDaoJson;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(laborDemandController).build();

        // 准备测试数据 - 劳务需求VO
        laborDemandVO = new LaborDemandVO();
        laborDemandVO.setId(1);
        laborDemandVO.setProjectId(1);
        laborDemandVO.setProjectName("测试项目");
        laborDemandVO.setJobTypeId(1);
        laborDemandVO.setJobTypeName("木工");
        laborDemandVO.setHeadcount(10);
        laborDemandVO.setDailyWage(new BigDecimal("300.00"));
        laborDemandVO.setStartDate(new Date());
        laborDemandVO.setEndDate(new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L)); // 30天后
        laborDemandVO.setWorkHours("8:00-18:00");
        laborDemandVO.setRequirements("有相关经验");
        laborDemandVO.setAccommodation(true);
        laborDemandVO.setMeals(true);
        laborDemandVO.setStatus("open");
        laborDemandVO.setCreateTime(new Date());
        laborDemandVO.setCompanyName("测试公司");

        // 准备测试数据 - 添加劳务需求DAO JSON
        laborDemandAddDaoJson = "{\"projectId\":1,\"jobTypeId\":1,\"headcount\":10,\"dailyWage\":300.00,\"startDate\":\"2025-04-15\",\"endDate\":\"2025-05-15\",\"workHours\":\"8:00-18:00\",\"requirements\":\"有相关经验\",\"accommodation\":true,\"meals\":true}";

        // 准备测试数据 - 更新劳务需求DAO JSON
        laborDemandUpdateDaoJson = "{\"id\":1,\"headcount\":15,\"dailyWage\":350.00}";

        // 准备测试数据 - 更新劳务需求状态DAO JSON
        statusUpdateDaoJson = "{\"id\":1,\"status\":\"filled\"}";

        // 准备测试数据 - 查询劳务需求DAO JSON
        laborDemandQueryDaoJson = "{\"pageNum\":1,\"pageSize\":10,\"projectId\":1}";
    }

    @Test
    @DisplayName("测试添加劳务需求")
    void testAddLaborDemand() throws Exception {
        // 使用MockedStatic模拟静态方法
        try (MockedStatic<CurrentUserUtils> mockedStatic = Mockito.mockStatic(CurrentUserUtils.class)) {
            // 模拟当前用户ID
            mockedStatic.when(CurrentUserUtils::getCurrentUserId).thenReturn(1L);
            
            // 模拟服务层方法
            when(laborDemandService.addLaborDemand(any(LaborDemandAddDao.class), anyLong())).thenReturn(laborDemandVO);

            // 执行测试并验证结果
            mockMvc.perform(post("/labor-demand/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(laborDemandAddDaoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.projectName", is("测试项目")));

            // 验证服务层方法被调用
            verify(laborDemandService, times(1)).addLaborDemand(any(LaborDemandAddDao.class), anyLong());
        }
    }

    @Test
    @DisplayName("测试获取劳务需求详情")
    void testGetLaborDemandInfo() throws Exception {
        // 模拟服务层方法
        when(laborDemandService.getLaborDemandInfo(anyInt())).thenReturn(laborDemandVO);

        // 执行测试并验证结果
        mockMvc.perform(get("/labor-demand/info/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", is(0)))
            .andExpect(jsonPath("$.data.id", is(1)))
            .andExpect(jsonPath("$.data.projectName", is("测试项目")));

        // 验证服务层方法被调用
        verify(laborDemandService, times(1)).getLaborDemandInfo(anyInt());
    }

    @Test
    @DisplayName("测试更新劳务需求")
    void testUpdateLaborDemand() throws Exception {
        // 使用MockedStatic模拟静态方法
        try (MockedStatic<CurrentUserUtils> mockedStatic = Mockito.mockStatic(CurrentUserUtils.class)) {
            // 模拟当前用户ID
            mockedStatic.when(CurrentUserUtils::getCurrentUserId).thenReturn(1L);
            
            // 模拟服务层方法
            when(laborDemandService.updateLaborDemand(any(LaborDemandUpdateDao.class), anyLong())).thenReturn(laborDemandVO);

            // 执行测试并验证结果
            mockMvc.perform(put("/labor-demand/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(laborDemandUpdateDaoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.projectName", is("测试项目")));

            // 验证服务层方法被调用
            verify(laborDemandService, times(1)).updateLaborDemand(any(LaborDemandUpdateDao.class), anyLong());
        }
    }

    @Test
    @DisplayName("测试更新劳务需求状态")
    void testUpdateLaborDemandStatus() throws Exception {
        // 使用MockedStatic模拟静态方法
        try (MockedStatic<CurrentUserUtils> mockedStatic = Mockito.mockStatic(CurrentUserUtils.class)) {
            // 模拟当前用户ID
            mockedStatic.when(CurrentUserUtils::getCurrentUserId).thenReturn(1L);
            
            // 模拟服务层方法
            when(laborDemandService.updateLaborDemandStatus(any(LaborDemandStatusUpdateDao.class), anyLong())).thenReturn(laborDemandVO);

            // 执行测试并验证结果
            mockMvc.perform(put("/labor-demand/status")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(statusUpdateDaoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.projectName", is("测试项目")));

            // 验证服务层方法被调用
            verify(laborDemandService, times(1)).updateLaborDemandStatus(any(LaborDemandStatusUpdateDao.class), anyLong());
        }
    }

    @Test
    @DisplayName("测试删除劳务需求")
    void testDeleteLaborDemand() throws Exception {
        // 使用MockedStatic模拟静态方法
        try (MockedStatic<CurrentUserUtils> mockedStatic = Mockito.mockStatic(CurrentUserUtils.class)) {
            // 模拟当前用户ID
            mockedStatic.when(CurrentUserUtils::getCurrentUserId).thenReturn(1L);
            
            // 模拟服务层方法
            when(laborDemandService.deleteLaborDemand(anyInt(), anyLong())).thenReturn(true);

            // 执行测试并验证结果
            mockMvc.perform(delete("/labor-demand/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data", is(true)));

            // 验证服务层方法被调用
            verify(laborDemandService, times(1)).deleteLaborDemand(anyInt(), anyLong());
        }
    }

    @Test
    @DisplayName("测试分页查询劳务需求")
    void testListLaborDemands() throws Exception {
        // 准备模拟的分页结果
        Page<LaborDemandVO> page = new Page<>(1, 10);
        List<LaborDemandVO> records = new ArrayList<>();
        records.add(laborDemandVO);
        page.setRecords(records);
        page.setTotal(1);
        
        // 模拟服务层方法
        when(laborDemandService.queryLaborDemandList(any(LaborDemandQueryDao.class))).thenReturn(page);

        // 执行测试并验证结果
        mockMvc.perform(post("/labor-demand/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(laborDemandQueryDaoJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", is(0)))
            .andExpect(jsonPath("$.data.records[0].id", is(1)))
            .andExpect(jsonPath("$.data.records[0].projectName", is("测试项目")));

        // 验证服务层方法被调用
        verify(laborDemandService, times(1)).queryLaborDemandList(any(LaborDemandQueryDao.class));
    }

    @Test
    @DisplayName("测试获取项目的所有劳务需求")
    void testGetLaborDemandsByProject() throws Exception {
        // 准备模拟数据
        List<LaborDemandVO> demands = new ArrayList<>();
        demands.add(laborDemandVO);
        
        // 模拟服务层方法
        when(laborDemandService.getLaborDemandsByProjectId(anyInt())).thenReturn(demands);

        // 执行测试并验证结果
        mockMvc.perform(get("/labor-demand/project/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code", is(0)))
            .andExpect(jsonPath("$.data[0].id", is(1)))
            .andExpect(jsonPath("$.data[0].projectName", is("测试项目")));

        // 验证服务层方法被调用
        verify(laborDemandService, times(1)).getLaborDemandsByProjectId(anyInt());
    }

    @Test
    @DisplayName("测试检查用户权限")
    void testCheckOperationPermission() throws Exception {
        // 使用MockedStatic模拟静态方法
        try (MockedStatic<CurrentUserUtils> mockedStatic = Mockito.mockStatic(CurrentUserUtils.class)) {
            // 模拟当前用户ID
            mockedStatic.when(CurrentUserUtils::getCurrentUserId).thenReturn(1L);
            
            // 模拟服务层方法
            when(laborDemandService.checkOperationPermission(anyInt(), anyLong())).thenReturn(true);

            // 执行测试并验证结果
            mockMvc.perform(get("/labor-demand/check-permission/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data", is(true)));

            // 验证服务层方法被调用
            verify(laborDemandService, times(1)).checkOperationPermission(anyInt(), anyLong());
        }
    }
}
