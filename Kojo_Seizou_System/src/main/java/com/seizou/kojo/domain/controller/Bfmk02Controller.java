package com.seizou.kojo.domain.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.seizou.kojo.domain.dto.CommonDto;
import com.seizou.kojo.domain.dto.UserInfoDto;
import com.seizou.kojo.domain.form.SearchForm;
import com.seizou.kojo.domain.service.Bfmk02Service;


/**
 * ユーザー情報一覧のControllerクラス
 */
@Controller
@RequestMapping("/b-forme_Kojo")
public class Bfmk02Controller {

	/** サービス */
	@Autowired
	Bfmk02Service service;

	//
	@Autowired
	MessageSource source;

	/**
	 * メニュー画面から遷移して来た時の処理
	 * @param commonDto
	 * @param form
	 * @param model
	 * @return bfmk02View
	 */
	@GetMapping("/pc/202")
	public String init(CommonDto commonDto, @ModelAttribute SearchForm form, Model model) {

		//メニュー画面の実装がないので仮に設定
		commonDto = new CommonDto("bfmk02", "ユーザーの情報一覧", "bfkt02", null, "bfm1", "us1", "総務部", "uskr000", "boss");  //権限区分:3(管理者)
		//commonDto = new CommonDto("bfmk02", "ユーザーの情報一覧", "bfkt02", null, "bfm1", "it1", "IT部", "itns004", "春夏 秋冬"); //権限区分:2(一般)

		//権限区分検索
		String authDiv = service.checkAuth(commonDto);
		System.out.println(authDiv);

		//権限チェック
		if ("1".equals(authDiv) || "2".equals(authDiv)) {
			// 権限区分がゲスト、一般の場合
			model.addAttribute("msinfo001", source.getMessage("msinfo001", null, Locale.JAPAN));

			//入力項目とボタンを非活性にする
			model.addAttribute("disabled", true);
		} else if ("3".equals(authDiv)) {
			// 権限区分が管理者の場合、自動で所属IDに値を設定する


			// 戻り値をModelに格納する
			form.setAffilicateId(authDiv);
			
			//所属IDの入力項目を非活性にする
			form.setAffilicateId(commonDto.getAffId());
		}
		return "bfmk02View";
	}

	/**
	 * 検索
	 * @param form
	 * @param model
	 * @return bfmk02View
	 */
	@PostMapping(path = "/pc/202",params = "search")
	public String search(@ModelAttribute SearchForm form, Model model) {
		
		//日付け入力値のチェックと検索処理
		if (service.dateFormat(form.getExpireDateFrom()) && service.dateFormat(form.getExpireDateTo())) {
			List<UserInfoDto> userList = service.getAllUserInfo(form);
			model.addAttribute("users",userList);
			return "bfmk02View";
		}
		
		//不正な入力値の処理
		model.addAttribute("msinfo002",source.getMessage("msinfo002", null, Locale.JAPAN));
		return "bfmk02View";
	}

	/**
	 * クリア
	 * @param commonDto
	 * @param form
	 * @param model
	 * @return bfmk02View
	 */
	@PostMapping(path = "/pc/202" ,params = "clear")
	public String clear(CommonDto dto, @ModelAttribute SearchForm form, Model model) {
		service.clearForm(form);
		init(dto, form, model);
		return "bfmk02View";
	}

	/**
	 * 戻る
	 * @param model
	 * @return bfkt02View
	 */
	@PostMapping(path = "/pc/202",params = "back")
	public String back(Model model) {
		return "redirect:/b-forme_Kojo/pc/002";
	}

	/**
	 * 削除
	 * @param form
	 * @param model
	 * @return
	 */
	@PostMapping(path = "/pc/202",params = "delete")
	public String delete(CommonDto commonDto,
			@RequestParam(value = "userIds", required = false) List<String> id, 
			@ModelAttribute SearchForm form, Model model) {

		// 削除対象未選択時
		if (id == null) {
			model.addAttribute("msinfo005",source.getMessage("msinfo005", null, Locale.JAPAN));
			clear(commonDto, form, model);
			System.out.println(model);
			return "bfmk02View";
		}

		//削除処理
		service.deleteUser(commonDto, id);
		clear(commonDto, form, model);
		model.addAttribute("msinfo007",source.getMessage("msinfo007", null, Locale.JAPAN));
		return "bfmk02View";
	}
}
