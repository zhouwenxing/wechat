package com.wechat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController {
	
	@RequestMapping(value = "")
	public String list() {
		return "欢迎来到微信公众号的世界";
	}

}
