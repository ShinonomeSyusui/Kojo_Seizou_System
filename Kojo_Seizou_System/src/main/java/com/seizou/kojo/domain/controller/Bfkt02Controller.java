package com.seizou.kojo.domain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
* メニュー画面表示のテスト用Controller
* @author K.Sakuma
*/
@Controller
@RequestMapping("/b-forme_Kojo")
public class Bfkt02Controller {

	// メニュー画面表示用GETコントローラー
	@GetMapping("/pc/002")
	public String init() {
		return "bfkt02View";
	}
	
	
}
