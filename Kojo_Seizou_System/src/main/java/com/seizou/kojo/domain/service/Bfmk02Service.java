package com.seizou.kojo.domain.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seizou.kojo.domain.dto.CommonDto;
import com.seizou.kojo.domain.dto.UserInfoDto;
import com.seizou.kojo.domain.form.PaginationForm;
import com.seizou.kojo.domain.form.SearchForm;
import com.seizou.kojo.domain.repository.Bfmk02Repository;

@Service
public class Bfmk02Service {

	@Autowired
	Bfmk02Repository repository;

	/**
	 * 権限チェック
	 * @param commonDto
	 * @return 参照・操作権限の有無判断
	 */
	public String checkAuth(CommonDto commonDto) {

		// 権限区分
		return repository.getAuthDiv(commonDto);
	}

	/**
	 * 全件数の取得
	 * @param form2
	 * @return count
	 */
	public List<Integer> getAllCount(SearchForm form, PaginationForm form2) {
		List<Integer> count = repository.searchInfoCount(form, form2);
		
		
		return count;
	}
	
	/**
	 * 検索
	 * @param form
	 * @return returnDtoList
	 */
	public List<UserInfoDto> getAllUserInfo(SearchForm form,PaginationForm form2){

		// 戻り値を宣言
		List<UserInfoDto> returnDtoList = new ArrayList<UserInfoDto>();

		// 検索実行
		List<Map<String, Object>> userMapList = repository.searchInfo(form,form2);

		// DTOに各値を設定
		for (Map<String, Object> map : userMapList) {
			UserInfoDto returnDto = new UserInfoDto();
			
			returnDto.setFacCd("bfm1");											// 工場CD
			returnDto.setAffilicateId((String)map.get("affilicate_id"));		// 所属ID
			returnDto.setAffilicateName((String)map.get("affilicate_name"));	// 所属名
			returnDto.setUserId((String)map.get("user_id"));					// ユーザーID
			returnDto.setUserName((String)map.get("user_name"));				// ユーザー名
			returnDto.setCount((Integer)map.get("count"));						// 所属人数
			returnDto.setAuthDiv((String)map.get("auth_div"));					// 権限区分
			returnDto.setWatchAuthFlg((String)map.get("watch_auth_flg"));		// 参照権限フラグ
			returnDto.setOprAuthFlg((String)map.get("opr_auth_flg"));			// 操作権限フラグ
			returnDto.setPass((String)map.get("pass"));							// パスワード

			// 日付の表示
			SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
			String from = (String)df.format(map.get("expire_date_from"));
			returnDto.setExpireDateFrom(from);									// 適用日（FROM）

			// 適用日（TO）の入力チェック
			if (!(map.get("expire_date_to") == null || map.get("expire_date_to") == "")) {
				String to = (String)df.format(map.get("expire_date_from"));
				returnDto.setExpireDateTo(to);									// 適用日（TO）
			}
			// リストに値を設定
			returnDtoList.add(returnDto);
		}
		return returnDtoList;
	}
	
	/**
	 * 全レコード数取得
	 * @param form
	 * @return allCou
	 */
	public int countAll(SearchForm form) {
		int allCou = repository.allCountSql(form);
		
		return allCou;
	}

	/**
	 * クリア
	 * @param form
	 * @return
	 */
	public SearchForm clearForm(SearchForm form) {
		
		// 各項目の初期化
		form.setAffilicateId("");
		form.setUserId("");
		form.setUserName("");
		form.setAuthDiv(null);
		form.setExpireDateFrom("");
		form.setExpireDateTo("");
		return form;
	}

	/**
	 * 削除
	 * @param commonDtoo
	 * @param id
	 * @return
	 */
	public String deleteUser(CommonDto commonDto, List<String> id) {
		
		// 入力チェック
		if (id != null) {
			
			//削除処理
			for (String idNum : id) {
				repository.delete(idNum);
			}
		}
		return "";
	}

	/**
	 * 入力チェック、日付け型変換
	 * @param form
	 * @return 日付け形式と空文字の時にtrue
	 */
	public boolean dateFormat(String date) {
		
		// 空文字チェック
		if (date.isBlank() || date.isEmpty()) {
			return false;
		}
		
		//受け取り側のフォーマット指定
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		// 有効日の表示型をフォーマット
		try {
			Date d = format.parse(date);
			System.out.println(d);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 未来日チェック
	 * @param date
	 * @return 
	 */
	public boolean miraibicheck(String date) {

		//今日の日付けを取得
		Date today = new Date();
		
		//受け取り値のフォーマット
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date inputDay = format.parse(date);
			return inputDay.after(today);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 次ページへの処理
	 * @param form2
	 * @return returnForm2
	 */
	public PaginationForm nextPage(PaginationForm form2) {
		
		//戻り値の宣言
		PaginationForm returnForm2 = new PaginationForm();
		
		int currntPage = 0;				//現在のページ
		int limit = 5;					//リミット値(1ページあたりの表示レコード数)
		int maxPage = 0;				//最大ページ数
		int count = form2.getCount();	//全レコード数
		
		maxPage = count / limit;
		
		//offsetの値
		int offsetNum = form2.getOffset();
		if (offsetNum >= 0) {
			++offsetNum;
			returnForm2.setOffset(offsetNum);
		}
		return returnForm2;
	}
	
	public int totalPages(PaginationForm form2,int count) {

		//戻り値を宣言
		int totalPagesNum = 0;
		
		totalPagesNum = count / form2.getLimit();

		if (totalPagesNum % form2.getLimit() != 0 
				|| totalPagesNum == 0) {
			++totalPagesNum;
		}
		return totalPagesNum;
	}
}
