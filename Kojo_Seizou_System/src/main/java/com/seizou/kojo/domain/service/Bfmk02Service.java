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
import com.seizou.kojo.domain.dto.PaginationDto;
import com.seizou.kojo.domain.dto.UserInfoDto;
import com.seizou.kojo.domain.form.SearchForm;
import com.seizou.kojo.domain.repository.Bfmk02Repository;

@Service
public class Bfmk02Service {

	/** リポジトリ */
	@Autowired
	Bfmk02Repository repository;

	/**
	 * 権限取得
	 * @param commonDto
	 * @return 参照・操作権限の有無判断
	 */
	public String getAuthority(CommonDto commonDto) {

		// 権限区分
		return repository.getAuthDiv(commonDto);
	}

	/**
	 * 検索
	 * @param form
	 * @param pageDto
	 * @return returnDtoList
	 */
	public List<UserInfoDto> getAllUserInfo(SearchForm form,
			PaginationDto pageDto) {

		// 戻り値を宣言
		List<UserInfoDto> returnDtoList = new ArrayList<UserInfoDto>();

		// 検索実行
		List<Map<String, Object>> userMapList = repository.searchInfo(form,
				pageDto);

		// DTOに各値を設定
		for (Map<String, Object> map : userMapList) {
			UserInfoDto returnDto = new UserInfoDto();

			returnDto.setFacCd("bfm1"); // 工場CD
			returnDto.setAffilicateId((String) map.get("affilicate_id")); // 所属ID
			returnDto.setAffilicateName((String) map.get("affilicate_name")); // 所属名
			returnDto.setUserId((String) map.get("user_id")); // ユーザーID
			returnDto.setUserName((String) map.get("user_name")); // ユーザー名
			returnDto.setCount((Integer) map.get("count")); // 所属人数
			returnDto.setAuthDiv((String) map.get("auth_div")); // 権限区分
			returnDto.setWatchAuthFlg((String) map.get("watch_auth_flg")); // 参照権限フラグ
			returnDto.setOprAuthFlg((String) map.get("opr_auth_flg")); // 操作権限フラグ
			returnDto.setPass((String) map.get("pass")); // パスワード

			// 日付の表示
			SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
			String from = (String) df.format(map.get("expire_date_from"));
			returnDto.setExpireDateFrom(from); // 適用日（FROM）

			// 適用日（TO）の入力チェック
			if (!(map.get("expire_date_to") == null
					|| map.get("expire_date_to") == "")) {
				String to = (String) df.format(map.get("expire_date_from"));
				returnDto.setExpireDateTo(to); // 適用日（TO）
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
	public int getAllCount(SearchForm form) {
		int allCou = repository.getTotalCount(form);

		return allCou;
	}

	/**
	 * 削除
	 * @param id
	 */
	public void deleteUser(List<String> id) {

		// 入力チェック
		if (id != null) {

			// 削除処理
			for (String idNum : id) {
				repository.delete(idNum);
			}
		}
	}

	/**
	 * 日付け型変換
	 * @param date
	 * @return 変換に成功時 true、それ以外は false
	 */
	public boolean isDate(String date) {

		// 入力チェック 未入力時はtrueを返す
		if (date.isBlank()) {
			return true;
		}

		// 受け取り側のフォーマット指定
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 有効日の表示型をフォーマット
		try {
			sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 未来日チェック
	 * @param fromDay
	 * @param toDay
	 * @return 未来日なら true、それ以外は false
	 */
	public boolean futureDateCheck(String fromDay, String toDay) {

		// 入力チェック 未入力時はfalseを返す
		if (fromDay.isBlank() || toDay.isBlank()) {
			return false;
		}

		// フォーマットの型指定
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		fmt.setLenient(false); // 厳密な日付けフォーマットチェックをするために設定

		try {
			// 受け取った日付けを解析
			Date inputDayFrom = fmt.parse(fromDay);
			Date inputDayTo = fmt.parse(toDay);

			// 今日の日付けの比較用（時刻を除いた日付けのみ）
			Date today = fmt.parse(fmt.format(inputDayTo));

			// 未来日ならばtrueを返す
			return inputDayFrom.after(today);

		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 最も古い日付けを取得する処理
	 * @return oldestate
	 */
	public Date getOldestDate() {

		// 戻り値の宣言
		Date oldestate = new Date(Long.MAX_VALUE);

		// DBから取得した日付リスト
		List<Map<String, Object>> days = repository.getAllFromDate();

		// 各レコードの日付をチェック
		for (Map<String, Object> day : days) {
			Date expireDateFrom = (Date) day.get("expire_date_from");

			// 古い日付と比較して、より古ければ更新
			if (expireDateFrom != null
					&& expireDateFrom.before(oldestate)) {
				oldestate = expireDateFrom;
			}
		}
		return oldestate;
	}
}
