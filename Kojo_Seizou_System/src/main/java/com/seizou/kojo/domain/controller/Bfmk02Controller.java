package com.seizou.kojo.domain.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.seizou.kojo.domain.form.PaginationForm;
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
		//commonDto = new CommonDto("bfmk02", "ユーザーの情報一覧", "bfkt02", null, "bfm1", "us1", "総務部", "uskr000", "boss");  //権限区分:3(管理者)
		//commonDto = new CommonDto("bfmk02", "ユーザーの情報一覧", "bfkt02", null, "bfm1", "it1", "IT部", "itns004", "春夏 秋冬"); //権限区分:2(一般)
		commonDto = new CommonDto("bfmk02", "ユーザーの情報一覧", "bfkt02", null, "bfm1", "all", "*****", "al00000", "admin"); //権限区分:4(アドミン)

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

		//今日の日付けをフォームへセット
		String kyou = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		kyou = format.format(today);

		//Formに今日の日付けをセット
		form.setExpireDateFrom(kyou);

		return "bfmk02View";
	}

	/**
	 * 検索
	 * @param form
	 * @param model
	 * @return bfmk02View
	 */
	@PostMapping(path = "/pc/202", params = "offset")
	public String search(@ModelAttribute SearchForm form, PaginationForm form2, Model model) {

		//日付け入力値のチェックと検索処理
		if (service.dateFormat(form.getExpireDateFrom()) && service.dateFormat(form.getExpireDateTo())) {

			//不正な入力値の処理
			model.addAttribute("msinfo002", source.getMessage("msinfo002", null, Locale.JAPAN));
			return "bfmk02View";
		}

		//未来日チェック
		if (service.miraibicheck(form.getExpireDateFrom()) || service.miraibicheck(form.getExpireDateTo())) {

			//未来日エラーの処理
			model.addAttribute("msinfo004", source.getMessage("msinfo004", null, Locale.JAPAN));
			return "bfmk02View";
		}

		List<Integer> pages = new ArrayList<Integer>();

		//ページネイションの内部処理
		pages = pagination(form, form2);


		//検索処理
		List<UserInfoDto> userList = service.getAllUserInfo(form, form2);

		model.addAttribute("users", userList);
		model.addAttribute("count", pages.get(0));
		model.addAttribute("totalPages", pages.get(1));
		model.addAttribute("currentPage", pages.get(2));
		model.addAttribute("next",pages.get(3));
		model.addAttribute("prev",pages.get(4));

		//pagination(form, form2, model);
		
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
		service.clearForm(form);
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
	 * 削除
	 * @param form
	 * @param model
	 * @return
	 */
	@PostMapping(path = "/pc/202", params = "delete")
	public String delete(CommonDto commonDto,
			@RequestParam(value = "userIds", required = false) List<String> id,
			@ModelAttribute SearchForm form, Model model) {

		// 削除対象未選択時
		if (id == null) {
			model.addAttribute("msinfo005", source.getMessage("msinfo005", null, Locale.JAPAN));
			clear(commonDto, form, model);
			System.out.println(model);
			return "bfmk02View";
		}

		//削除処理
		service.deleteUser(commonDto, id);
		clear(commonDto, form, model);
		model.addAttribute("msinfo007", source.getMessage("msinfo007", null, Locale.JAPAN));
		return "bfmk02View";
	}

	/**
	 * ページネイションの最初へ
	 * @param pageFrom
	 * @return 
	 */
	@PostMapping(path = "/pc/202", params = "first")
	public String paginationFrom(@RequestParam(value = "first") int offset,
			@ModelAttribute SearchForm form,
			PaginationForm form2, Model model) {
		form2.setOffset(offset);
		search(form, form2, model);
		return "bfmk02View";
	}

//	/**
//	 * ページネイションの前へ
//	 * @param pageFrom
//	 * @return 
//	 */
//	@PostMapping(path = "/pc/202", params = "prev")
//	public String paginationPrev(@RequestParam(value = "prev") int offset,
//			@ModelAttribute SearchForm form,
//			PaginationForm form2, Model model) {
//		form2.setOffset(offset);
//		search(form, form2, model);
//		return "bfmk02View";
//	}

	/**
	 * ページネイションの次へ
	 * @param pageFrom
	 * @return 
	 */
	@PostMapping(path = "/pc/202", params = "next")
	public String paginationNextAndPrev(@RequestParam(value = "next") int next,
			@ModelAttribute SearchForm form,
			PaginationForm form2, Model model) {

		int nextNum = 1;
		form2.setNext(nextNum);

		//検索処理
		search(form, form2, model);

		//

		return "bfmk02View";
	}

	/**
	 * ページネイションの最後へ
	 * @param pageFrom
	 * @return 
	 */
	@PostMapping(path = "/pc/202", params = "last")
	public String paginationLast(@RequestParam(value = "last") int offset,
			@ModelAttribute SearchForm form,
			PaginationForm form2, Model model) {
		form2.setOffset(offset);
		search(form, form2, model);
		return "bfmk02View";
	}

	/**
	 * ページネイションの内部処理
	 * @param form
	 * @param form2
	 * @return pages
	 */
	private List<Integer> pagination(SearchForm form, PaginationForm form2) {

		//戻り値を宣言
		List<Integer> pages = new ArrayList<Integer>();

		//1ページにおける表示件数
		final int LIMIT = 5;
		form2.setLimit(LIMIT);

		//全レコード数
		int count = service.countAll(form);

		//総ページ数
		int totalPages = count / LIMIT;
		if (totalPages % LIMIT != 0 ||
				totalPages == 0) {
			++totalPages;
		}

		//現在ページ
		int offset = form2.getOffset();
		int currentPage = offset / LIMIT + 1;

		//次のoffset値
		int nextNum = offset + LIMIT;
		
		//前のoffset値
		int prevNum = offset - LIMIT;
		
		pages.add(count);
		pages.add(totalPages);
		pages.add(currentPage);
		pages.add(nextNum);
		pages.add(prevNum);
		
		//モデルに格納
		//model.addAttribute("count", count);
		//model.addAttribute("totalPages", totalPages);
		//model.addAttribute("currentPage", currentPage);
		//model.addAttribute("next", nextNum);
		//model.addAttribute("prev", prevNum);
		
		return pages;
	}
}
