package com.onlinepowers.springmybatis.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/test")
    public String hello() {
        return "list";
    }

    //목록
    @RequestMapping("/list")
    public String getUserList(Model model) {
        List<UserDto> userList = userService.getUserList();
        model.addAttribute("userList", userList);
        return "list";
    }

    //글쓰기 폼으로 이동
    @RequestMapping( "/register")
    public String register() {
        return "register";
    }

    //게시글 등록
    @RequestMapping("/registerUser")
    public String registerUser(UserDto user) {
        userService.insertUser(user);
        return "redirect:/list";
    }

    // 수정 폼으로 이동
    @GetMapping(value="/edit/{no}")
    public String edit(@PathVariable("no") Integer id, UserDto user, Model model) {
        UserDto userDto = userService.getUser(id);
        BeanUtils.copyProperties(userDto, userDto);
        model.addAttribute("userDto" , userDto);
        return "edit";
    }

    // 최종 수정 버튼 누름
    @GetMapping(value = "edit")
    public String updateUser(@RequestParam String id, @Validated UserDto user, BindingResult result) {
        userService.updateUser(user);
        return "redirect:/list";
    }

    //특정 고객정보 삭제
    @PostMapping(value = "delete/{no}")
    String deleteUser(@PathVariable("no") Integer id) {
        userService.deleteUser(id);
        return "redirect:/list";
    }

    //돌아가기
    @RequestMapping("/return")
    String goToList() {
        return "redirect:/list";
    }
    @RequestMapping("/edit/return")
    String goToList1() {
        return "redirect:/list";
    }

}
