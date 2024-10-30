package com.seizou.kojo.domain.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.seizou.kojo.domain.dto.CommonDto;
import com.seizou.kojo.domain.dto.PaginationDto;
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

	/** メッセージソース */
	@Autowired
	MessageSource source;

	/**
	 * 初期画面（メニュー画面から遷移して来た時の処理）
	 * @param commonDto
	 * @param form
	 * @param model
	 * @return bfmk02View
	 */
	@GetMapping("/pc/202")
	public String init(CommonDto commonDto, @ModelAttribute SearchForm form, Model model) {

		//既存のログイン処理の仕様で、ログインユーザーの情報がこの画面に渡されない前提となっているため
		//コード上でユーザーを切り替えてテストする
		//commonDto = new CommonDto("bfmk02", "ユーザーの情報一覧", "bfkt02", null, "bfm1", "us1", "総務部", "uskr000", "boss");      //権限区分:3(管理者)
		//commonDto = new CommonDto("bfmk02", "ユーザーの情報一覧", "bfkt02", null, "bfm1", "it1", "IT部", "itns004", "春夏 秋冬");   //権限区分:2(一般)
		commonDto = new CommonDto("bfmk02", "ユーザーの情報一覧", "bfkt02", null, "bfm1", "all", "*****", "al00000", "admin");      //権限区分:4(アドミン)

		//権限区分検索
		String authDiv = service.getAuthority(commonDto);

		//権限チェック
		if ("1".equals(authDiv) || "2".equals(authDiv)) {
		  
			// 権限区分がゲスト、一般の場合エラーメッセージをmodelに格納
			model.addAttribute("msinfo001", source.getMessage("msinfo001", null, Locale.JAPAN));

			//ボタンを非活性にする
			model.addAttribute("btnDisabled", true);
			model.addAttribute("affIdReadonly",true);
			model.addAttribute("userReadonly",true);

		} else if ("3".equals(authDiv)) {

			// 権限区分が管理者の場合、自動で所属IDに値を設定する
			form.setAffilicateId(commonDto.getAffId());

			//所属IDの入力項目を非活性にする
			model.addAttribute("affIdReadonly",true);
		}

		//今日の日付けをフォームへセット
		String toDay = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		toDay = format.format(today);
		
		//DBから取得した最も古い日付け
		Date oldDays = service.getOldestDate();
		String oldDay = format.format(oldDays);

		//Formに今日の日付けと最も古い日付けをセット
		form.setExpireDateTo(toDay);
		form.setExpireDateFrom(oldDay);

		return "bfmk02View";
	}

	/**
	 * 検索
	 * @param form
	 * @param pageDto
	 * @param model
	 * @return bfmk02View
	 */
	@PostMapping(path = "/pc/202", params = "offset")
	public String search(@ModelAttribute SearchForm form, PaginationDto pageDto, Model model) {

		//日付け入力値のチェック
		if (!(service.isDate(form.getExpireDateFrom()) && service.isDate(form.getExpireDateFrom()))){

			//不正な入力値の処理、エラーメッセージをmodelに格納し画面表示
			model.addAttribute("msinfo002", source.getMessage("msinfo002", null, Locale.JAPAN));
			return "bfmk02View";
		}

		//未来日チェック
		if (service.futureDateCheck(form.getExpireDateFrom(), form.getExpireDateTo())) {

			//未来日エラーの処理、エラーメッセージをmodelに格納し画面表示
			model.addAttribute("msinfo004", source.getMessage("msinfo004", null, Locale.JAPAN));
			return "bfmk02View";
		}

		//ページネイションの内部処理
		pagination(form, pageDto, model);

		//検索処理
		List<UserInfoDto> userList = service.getAllUserInfo(form, pageDto);

		//検索結果が該当なしの表示処理
		if (userList.isEmpty()) {
			model.addAttribute("msinfo008",source.getMessage("msinfo008", null, Locale.JAPAN));
			return init(null, form, model);
		}
		
		model.addAttribute("users", userList);

		return "bfmk02View";
	}

	/**
	 * 削除
	 * @param id
	 * @param form
	 * @param pageDto
	 * @param model
	 * @return bfmk02View
	 */
	@PostMapping(path = "/pc/202/{offset}", params = "delete")
	public String delete(
			@RequestParam(value = "sendUserId", required = false) List<String> id,
			@PathVariable int offset, @ModelAttribute SearchForm form,
			PaginationDto pageDto, Model model) {

		// 削除対象未選択時
		if (id == null) {
			model.addAttribute("msinfo005",
					source.getMessage("msinfo005", null, Locale.JAPAN));
			pageDto.setOffset(offset);
			search(form, pageDto, model);
			return "bfmk02View";
		}

		// 削除処理
		service.deleteUser(id);
		pageDto.setOffset(offset);
		search(form, pageDto, model);
		model.addAttribute("msinfo007",
				source.getMessage("msinfo007", null, Locale.JAPAN));
		return "bfmk02View";
	}

	/**
	 * クリア
	 * @param commonDto
	 * @param form
	 * @param model
	 * @return bfmk02View
	 */
	@PostMapping(path = "/pc/202", params = "clear")
	public String clear(CommonDto dto, @ModelAttribute SearchForm form, Model model) {
		clearForm(form);
		init(dto, form, model);
		return "bfmk02View";
	}

	/**
	 * 戻る
	 * @param model
	 * @return bfkt02View
	 */
	@PostMapping(path = "/pc/202", params = "back")
	public String back(Model model) {
		return "redirect:/b-forme_Kojo/pc/002";
	}

	/**
	 * ページネイションの内部処理
	 * @param form
	 * @param pageDto
	 * @param model
	 */
	private void pagination(SearchForm form, PaginationDto pageDto, Model model) {

		//1ページにおける表示件数
		final int LIMIT = 5;
		pageDto.setLimit(LIMIT);

		//全レコード数
		int count = service.getAllCount(form);

		//現在ページ
		int offset = pageDto.getOffset();
		int currentPage = offset / LIMIT + 1;
		
		//最初のページのoffset値
		int firstNum = 0;

		//次のoffset値
		int nextNum = offset + LIMIT;

		//前のoffset値
		int prevNum = offset - LIMIT;

		//最初のページと戻るボタンの非表示処理
		if (currentPage <= 1) {
			model.addAttribute("firstPage",true);
		}

		//件数制限処理
		final int MAX_COUNT = 50;
		if (count > MAX_COUNT) {
		  model.addAttribute("msinfo006", source.getMessage("msinfo006", null, Locale.JAPAN));
		  count = MAX_COUNT;
		}
		
		//総ページ数
		int totalPages = (int)Math.ceil((double) count / LIMIT);

		//最終ページのoffset値
		int last = (totalPages - 1) * LIMIT;

		//最後のページと進むボタンの非表示処理
		if (currentPage == totalPages) {
		    model.addAttribute("lastPage", true);
		}
		
		model.addAttribute("count", count);					//総レコード数
		model.addAttribute("totalPages", totalPages);		//全ページ数
		model.addAttribute("currentPage", currentPage);		//現在ページ
		model.addAttribute("first", firstNum);				//最初のページのoffset値
		model.addAttribute("prev", prevNum);				//前のページのoffset値
		model.addAttribute("next", nextNum);				//次のページのoffset値
		model.addAttribute("last", last);					//最後のページのoffset値
		model.addAttribute("offset",offset);				//offset値
	}
	
	/**
	 * Form初期化
	 * @param form
	 */
	private void clearForm(SearchForm form) {

		// 各項目の初期化
		form.setAffilicateId("");
		form.setUserId("");
		form.setUserName("");
		form.setAuthDiv(null);
		form.setExpireDateFrom("");
		form.setExpireDateTo("");
	}
}
