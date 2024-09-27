package com.seizou.kojo.domain.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.seizou.kojo.domain.dto.CommonDto;
import com.seizou.kojo.domain.form.PaginationForm;
import com.seizou.kojo.domain.form.SearchForm;


@Repository
public class Bfmk02Repository {

	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	NamedParameterJdbcTemplate namedJdbc;

	/**
	 * 権限チェック
	 * @param commonDto
	 * @return returnValue;
	 */
	public String getAuthDiv(CommonDto commonDto) {

		String sql = "SELECT "
				+ "ui.auth_div "			//権限区分
				+ "FROM "
				+ "user_info AS ui "
				+ "WHERE "
				+ "ui.fac_cd = ? "			//工場CD
				+ "AND "
				+ "ui.affilicate_id = ? "	//部署ID
				+ "AND "
				+ "ui.user_id = ? ";		//ユーザーID
		Map<String, Object> authDiv = jdbcTemplate.queryForMap(sql, commonDto.getFacCd(), commonDto.getAffId(), commonDto.getUserId());

		String returnValue = (String)authDiv.get("auth_div");
		return returnValue;
	}
	
	/**
	 * 条件検索
	 * @param form
	 * @return namedJdbc.queryForList(sql,joken);
	 */
	public List<Map<String, Object>> searchInfo(SearchForm form,PaginationForm form2) {
		
		String sql = "SELECT "
				+ "u.affilicate_id "							//所属ID
				+ ",b.affilicate_name "							//所属名
				+ ",u.user_id "									//ユーザーID
				+ ",u.user_name "								//ユーザー名
				+ ",u.expire_date_from "						//有効日(From)
				+ ",u.expire_date_to "							//有効日(To)
				+ ",CASE "
				+ "WHEN  u.auth_div = 1 THEN 'ゲスト' "
				+ "WHEN  u.auth_div = 2 THEN '一般' "
				+ "WHEN  u.auth_div = 3 THEN '管理者' "
				+ "WHEN  u.auth_div = 4 THEN 'admin' "
				+ "ELSE '対象なし' "
				+ "END AS auth_div "							//権限区分
				+ ",CASE "
				+ "WHEN u.watch_auth_flg = true THEN '有' "
				+ "WHEN u.watch_auth_flg = false THEN '無' "
				+ "ELSE '不明' "
				+ "END AS watch_auth_flg "						//参照権限
				+ ",CASE "
				+ "WHEN u.opr_auth_flg = true THEN '有' "
				+ "WHEN u.opr_auth_flg = false THEN '無' "
				+ "ELSE '不明' "
				+ "END AS opr_auth_flg "						//操作権限
				+ ",u.pass "									//パスワード
				+ "FROM "
				+ "user_info AS u "
				+ "LEFT JOIN "
				+ "belonging AS b "
				+ "ON "
				+ "u.affilicate_id = b.affilicate_id "
				+ "WHERE "
				+ "u.del_flg = '0' ";							//削除フラグ

		List<Object> returnJouken = joukenSql(form);

		sql += returnJouken.get(0);

		Map<String, Object> joken = (Map<String, Object>) returnJouken.get(1);

		sql += "ORDER BY u.affilicate_id ,u.user_id "
				+ "LIMIT :limit OFFSET :offset";
		joken.put("offset",form2.getOffset());
		joken.put("limit",form2.getLimit());

		return namedJdbc.queryForList(sql,joken);
	}

	/**
	 * 削除
	 * @param userId
	 * @return jdbcTemplate.update(sql,userId);
	 */
	public int delete(String userId) {
		String sql = "UPDATE "
				+ "user_info "
				+ "SET "
				+ "del_flg = '1' "		//削除フラグ
				+ "WHERE "
				+ "user_id = ?";		//ユーザーID
		return jdbcTemplate.update(sql,userId);
	}
	
	/**
	 * 検索条件
	 * @param form
	 * @return returnObject
	 */
	private List<Object> joukenSql(SearchForm form) {
		
		String sql = "";
		
		Map<String, Object> joken = new HashMap<String, Object>();
		
		// 所属ID
		if (!(form.getAffilicateId().isBlank() || form.getAffilicateId().isEmpty())) {
			sql += "AND u.affilicate_id = :affilicateId ";
			joken.put("affilicateId", form.getAffilicateId());
		}

		// ユーザーID
		if (!(form.getUserId().isBlank() || form.getUserId().isEmpty())) {
			sql += "AND u.user_id = :userId ";
			joken.put("userId", form.getUserId());
		}

		// ユーザー名
		if (!(form.getUserName().isBlank() || form.getUserName().isEmpty())) {
			sql += "AND u.user_name LIKE :userName ";
			joken.put("userName", "%" + form.getUserName() + "%");
		}

		// 権限区分
		if (!(form.getAuthDiv().size() == 0 || form.getAuthDiv().size() == 3)) {
			sql += "AND u.auth_div IN (:authDiv) ";
			joken.put("authDiv", form.getAuthDiv());
		}

		// 有効日(From)
		if (!(form.getExpireDateFrom().isBlank() || form.getExpireDateFrom().isEmpty())) {
			sql += "AND u.expire_date_from = :expireDateFrom ";
			joken.put("expireDateFrom", form.getExpireDateFrom());
		}

		// 有効日(To)
		if (!(form.getExpireDateTo().isBlank() || form.getExpireDateTo().isEmpty())) {
			sql += "AND u.expire_date_to = :expireDateTo ";
			joken.put("expireDateTo", form.getExpireDateTo());
		}
		
		List<Object> returnObject = new ArrayList<>();
		
		returnObject.add(sql);
		returnObject.add(joken);
		
		return returnObject;
	}
	
	/**
	 * 全件数を取得する処理
	 * @param joken
	 * @return
	 */
	public int allCountSql(SearchForm form) {

		String sql = "SELECT "
				+ "COUNT(u.affilicate_id) "
				+ "FROM "
				+ "user_info AS u "
				+ "LEFT JOIN "
				+ "belonging AS b "
				+ "ON "
				+ "u.affilicate_id = b.affilicate_id "
				+ "WHERE "
				+ "u.del_flg = '0' ";							//削除フラグ
		
		List<Object> returnJouken = joukenSql(form);

		sql += returnJouken.get(0);

		Map<String, Object> joken = (Map<String, Object>) returnJouken.get(1);

		int countAll = namedJdbc.queryForObject(sql, joken, Integer.class);
		return countAll;
	}
}
