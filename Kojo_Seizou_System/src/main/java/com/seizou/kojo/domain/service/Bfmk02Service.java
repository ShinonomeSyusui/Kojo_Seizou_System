package com.seizou.kojo.domain.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seizou.kojo.domain.dao.UserInfoDao;
import com.seizou.kojo.domain.dto.CommonDto;
import com.seizou.kojo.domain.dto.UserInfoDto;
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
	 * 
	 * @param dao
	 * @return
	 */
	public String getSearchUser(UserInfoDao dao) {
		String authority = "";
		dao = (UserInfoDao) repository.getAllUserInfo();
		return "";
	}

	/**
	 * 検索
	 * @param form
	 * @return List<UserInfoDto>
	 */
	public List<UserInfoDto> getAllUserInfo(SearchForm form){

		// 戻り値を宣言
		List<UserInfoDto> returnDtoList = new ArrayList<UserInfoDto>();

		// 検索実行
		List<Map<String, Object>> userMapList = repository.searchInfo(form);

		// DTOに各値を設定
		for (Map<String, Object> map : userMapList) {
			UserInfoDto returnDto = new UserInfoDto();
			
			returnDto.setFacCd("bfm1");											// 工場CD
			returnDto.setAffilicateId((String)map.get("affilicate_id"));		// 所属ID
			returnDto.setAffilicateName((String)map.get("affilicate_name_r"));	// 所属名
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
	 * クリアボタンが押された時の処理
	 * @param form
	 * @return
	 */
	public SearchForm clearForm(SearchForm form) {
		
		//各項目の初期化
		form.setAffilicateId("");
		form.setUserId("");
		form.setUserName("");
		form.setAuthDiv(null);
		form.setExpireDateFrom("");
		form.setExpireDateTo("");
		return form;
	}
	
	/**
	 * 削除ボタンが押下された時の処理
	 * @param commonDtoo
	 * @param id
	 * @return
	 */
	public String deleteUser(CommonDto commonDto, List<String> id) {
		
		//入力チェック
		if (id != null) {
			
			//削除処理
			for (String idNum : id) {
				repository.delete(idNum);
			}
		}
		return "";
	}
}
