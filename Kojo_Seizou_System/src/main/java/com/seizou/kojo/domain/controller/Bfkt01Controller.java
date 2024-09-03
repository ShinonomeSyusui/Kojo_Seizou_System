package com.seizou.kojo.domain.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
* ログイン画面用Controller
* @author K.Sakuma
*/
@Controller
@RequestMapping("/b-forme_Kojo")
public class Bfkt01Controller {

	// ログイン画面表示用GETコントローラー
	@GetMapping("/pc/001")
	public String init() {
		return "bfkt01View";
	}
		
		
}
