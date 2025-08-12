package com.hugh.base.service.stability.service;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 统一稳定性管理服务
 * 集成Sentinel限流熔断功能
 */
@Slf4j
@Service
public class UnifiedStabilityService {

    @PostConstruct
    public void init() {
        log.info("Initializing unified stability service");
        initFlowRules();
        initDegradeRules();
    }

    /**
     * 初始化限流规则
     */
    private void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();

        // 默认限流规则
        FlowRule rule = new FlowRule();
        rule.setResource("defaultResource");
        rule.setGrade(1); // QPS限流
        rule.setCount(100); // 100次/秒
        rules.add(rule);

        FlowRuleManager.loadRules(rules);
        log.info("Loaded {} flow rules", rules.size());
    }

    /**
     * 初始化熔断规则
     */
    private void initDegradeRules() {
        List<DegradeRule> rules = new ArrayList<>();

        // 默认熔断规则
        DegradeRule rule = new DegradeRule();
        rule.setResource("defaultResource");
        rule.setGrade(2); // 异常比例
        rule.setCount(0.1); // 异常比例超过10%
        rule.setTimeWindow(10); // 熔断10秒
        rules.add(rule);

        DegradeRuleManager.loadRules(rules);
        log.info("Loaded {} degrade rules", rules.size());
    }

    /**
     * 动态更新限流规则
     * @param rules 新的限流规则
     */
    public void updateFlowRules(List<FlowRule> rules) {
        FlowRuleManager.loadRules(rules);
        log.info("Updated flow rules, count: {}", rules.size());
    }

    /**
     * 动态更新熔断规则
     * @param rules 新的熔断规则
     */
    public void updateDegradeRules(List<DegradeRule> rules) {
        DegradeRuleManager.loadRules(rules);
        log.info("Updated degrade rules, count: {}", rules.size());
    }
}
