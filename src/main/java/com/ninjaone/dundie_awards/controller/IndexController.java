package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.service.ActivityService;
import com.ninjaone.dundie_awards.service.EmployeeService;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class IndexController extends Controller {
    private final EmployeeService employeeService;
    private final ActivityService activityService;

    public IndexController(@NonNull EmployeeService employeeService,
                           @NonNull ActivityService activityService) {
        this.employeeService = employeeService;
        this.activityService = activityService;
    }

    @GetMapping("")
    public String getIndex(Model model) {
        model.addAttribute("employees", employeeService.findAll(PageRequest.of(0, MAX_PAGE_SIZE)));
        model.addAttribute("activities", activityService.findAll(PageRequest.of(0, MAX_PAGE_SIZE)));
        return "index";
    }
}
