package com.seizou.kojo.domain.repository;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.seizou.kojo.domain.dao.InsertUserInfoDao;

/**
* ユーザー情報 Repository
* @author K.Sakuma
*/
@Repository
public class Bfmk01Repository {
	
	@Autowired
	JdbcTemplate jdbc;
	
	/**
	 * 権限区分検索
	 * @param insertUserInfoDao
	 * @return authDiv
	 */
	public String authDivSeach(InsertUserInfoDao insertUserInfoDao) {
	
		System.out.println("リポジトリー：authDivSeachメソッド開始");
		
		// 権限格納用の変数
		String authDiv = null;
		
		// 1件SELECT
		try {
			String sql = "SELECT auth_div"
					+ " FROM user_info"
					+ " WHERE affilicate_id = ?"
					+ "	 AND user_id = ?;";
			Map<String, Object> authDivMap = jdbc.queryForMap(sql
												, insertUserInfoDao.getAffilicateId()
												, insertUserInfoDao.getUserId());
			// 権限格納用の変数authDivに値を登録
			authDiv = (String)authDivMap.get("auth_div");
					
		} catch(DataAccessException e) {
			System.out.println("リポジトリー：authDivSeachメソッド：DataAccessException発生");
			
		} catch(Exception e) {
			System.out.println("リポジトリー：authDivSeachメソッド：Exception発生");
			
		}
		
		System.out.println("リポジトリー：authDivSeachメソッド終了");
	
		return authDiv;
		
	}
	
	/**
	 * ユーザー情報取得
	 * @param insertUserInfoDao
	 * @return insertUserInfoDaoSelectResult
	 */
	public InsertUserInfoDao getUserInfo(InsertUserInfoDao insertUserInfoDao) {
		
		System.out.println("リポジトリー：getUserInfoメソッド開始");
		
		// 戻り値用のDaoを宣言し、初期化する。
		InsertUserInfoDao insertUserInfoDaoSelectResult = new InsertUserInfoDao();
		
		// 1件SELECT
		try {
			String sql = "SELECT"
					+ "		affilicate_id"
					+ ",	user_id"
					+ ",	user_name"
					+ ",	pass"
					+ ",	hash_pass"
					+ ",	expire_date_from"
					+ ",	expire_date_to"
					+ ",	auth_div"
					+ ",	watch_auth_flg"
					+ ",	opr_auth_flg"
					+ " FROM user_info"
					+ " WHERE fac_cd = ?"
					+ "  AND affilicate_id = ?"
					+ "  AND user_id = ?;";
			Map<String, Object> userInfoMap = jdbc.queryForMap(sql
					, insertUserInfoDao.getFacCd()
					, insertUserInfoDao.getAffilicateId()
					, insertUserInfoDao.getUserId());

			// insertUserInfoDaoに値を登録
			insertUserInfoDaoSelectResult.setAffilicateId((String)userInfoMap.get("affilicate_id"));			// 所属ID
			insertUserInfoDaoSelectResult.setUserId((String)userInfoMap.get("user_id"));						// ユーザーID
			insertUserInfoDaoSelectResult.setUserName((String)userInfoMap.get("user_name"));					// ユーザー名
			insertUserInfoDaoSelectResult.setPass((String)userInfoMap.get("pass"));							// パスワード
			insertUserInfoDaoSelectResult.setRePass((String)userInfoMap.get("pass"));						// パスワード再確認
			insertUserInfoDaoSelectResult.setExpireDateFrom((Date)userInfoMap.get("expire_date_from"));		// 有効日（FROM）
			insertUserInfoDaoSelectResult.setExpireDateTo((Date)userInfoMap.get("expire_date_to"));			// 有効日（TO）
			insertUserInfoDaoSelectResult.setAuthDiv((String)userInfoMap.get("auth_div"));					// 権限区分(ゲスト：1、一般：2、管理者：3)
			insertUserInfoDaoSelectResult.setWatchAuthFlg((Boolean)userInfoMap.get("watch_auth_flg"));		// 参照権限フラグ（権限あり：true、権限なし：false）
			insertUserInfoDaoSelectResult.setOprAuthFlg((Boolean)userInfoMap.get("opr_auth_flg"));			// 操作権限フラグ（権限あり：true、権限なし：false）
			
		} catch(DataAccessException e) {
			System.out.println("リポジトリー：getUserInfoメソッド：DataAccessException発生");
			
		} catch(Exception e) {
			System.out.println("リポジトリー：getUserInfoメソッド：Exception発生");
			
		}
		
		System.out.println("リポジトリー：getUserInfoメソッド終了");
		
		return insertUserInfoDaoSelectResult;
		
	}
	
	/**
	 * 所属ID検索 （admin専用入力チェック用）
	 * @param insertUserInfoDao
	 * @return affilicateIdSeachResult
	 */
	public String affilicateIdSeach(InsertUserInfoDao insertUserInfoDao) {
		
		System.out.println("リポジトリー：affilicateIdSeachメソッド開始");
		
		// 戻り値用の変数
		String affilicateIdSeachResult = null;
		
		// 1件検索
		try {
			String sql = "SELECT affilicate_id"
					+ " FROM belonging"
					+ " WHERE affilicate_id = ?;";
			Map<String, Object> affilicateIdSeachMap = jdbc.queryForMap(sql
												, insertUserInfoDao.getAffilicateId());
			
			// insertUserInfoDaoに値を登録
			affilicateIdSeachResult = (String)affilicateIdSeachMap.get("affilicate_id");
			
		} catch(DataAccessException e) {
			System.out.println("リポジトリー：affilicateIdSeachメソッド：DataAccessException発生");
			
		} catch(Exception e) {
			System.out.println("リポジトリー：affilicateIdSeachメソッド：Exception発生");
			
		}
		
		System.out.println("リポジトリー：affilicateIdSeachメソッド終了");
		
		return affilicateIdSeachResult;
		
	}
	
	/**
	 * 区分値取得
	 * @param insertUserInfoDao
	 * @return division
	 */
	public String getDivision(InsertUserInfoDao insertUserInfoDao) {
		
		System.out.println("リポジトリー：getDivisionメソッド開始");
		
		// 権限格納用の変数
		String division = null;
		
		// 1件SELECT
		try {
			String sql = "SELECT div_cd"
					+ " FROM division"
					+ " WHERE fac_cd = ?"
					+ "  AND big_func_cd = 'USID'"
					+ "  AND middle_func_cd = ?;";
			Map<String, Object> getDivisionMap = jdbc.queryForMap(sql
													, insertUserInfoDao.getFacCd()
													, insertUserInfoDao.getAuthDiv());
			// 権限格納用の変数divisionに値を登録
			division = (String)getDivisionMap.get("div_cd");
			
		} catch(DataAccessException e) {
			System.out.println("リポジトリー：getDivisionメソッド：DataAccessException発生");
			
		} catch(Exception e) {
			System.out.println("リポジトリー：getDivisionメソッド：Exception発生");
			
		}
		
		System.out.println("リポジトリー：getDivisionメソッド終了");
		
		return division;
		
	}
	
	/**
	 * ユーザー情報連番取得
	 * @param insertUserInfoDao
	 * @return serialNum
	 */
	public String getUserInfoSerialNum(InsertUserInfoDao insertUserInfoDao) {
		
		System.out.println("リポジトリー：getUserInfoSerialNumメソッド開始");
		
		// 権限格納用の変数
		String serialNum = null;
		
		// 1件SELECT
		try {
			String sql = "SELECT SUBSTRING(user_id, 5, 3)"
					+ " FROM user_info"
					+ " WHERE user_id ="
					+ " ("
					+ "  SELECT MAX(user_id)"
					+ "  FROM user_info"
					+ "  WHERE fac_cd = ?"
					+ "   AND user_id like CONCAT(SUBSTRING(?, 1, 2), ?, '%')"
					+ " );";
			Map<String, Object> serialNumMap = jdbc.queryForMap(sql
												, insertUserInfoDao.getFacCd()
												, insertUserInfoDao.getAffilicateId()
												, insertUserInfoDao.getDivCd());
			// 権限格納用の変数serialNumに値を登録
			serialNum = (String)serialNumMap.get("SUBSTRING(user_id, 5, 3)");
			
		} catch(DataAccessException e) {
			System.out.println("リポジトリー：getUserInfoSerialNumメソッド：DataAccessException発生");
			
		} catch(Exception e) {
			System.out.println("リポジトリー：getUserInfoSerialNumメソッド：Exception発生");
			
		}
		
		System.out.println("リポジトリー：getUserInfoSerialNumメソッド終了");
		
		return serialNum;
		
	}
	
	/**
	 * 登録
	 * @param insertUserInfoDao
	 * @throws DuplicateKeyException
	 */
	public void userInfoRegister(InsertUserInfoDao insertUserInfoDao) throws DuplicateKeyException {
		
		System.out.println("リポジトリー：userInfoRegisterメソッド開始");
		
		// 1件INSERT
		try {
			String sql = "INSERT INTO user_info(fac_cd,"					// 工場CD
					+ " affilicate_id,"										// 所属ID
					+ " user_id,"											// ユーザーID
					+ " user_name,"											// ユーザー名
					+ " pass,"												// パスワード
					+ " hash_pass,"											// パスワード（暗号化）
					+ " expire_date_from,"									// 適用日（FROM）
					+ " expire_date_to,"									// 適用日（TO）
					+ " auth_div,"											// 権限区分(ゲスト：1、一般：2、管理者：3)
					+ " watch_auth_flg,"									// 参照権限フラグ（権限あり：true、権限なし：false）
					+ " opr_auth_flg,"										// 操作権限フラグ（権限あり：true、権限なし：false）
					+ " del_flg,"											// 削除フラグ
					+ " create_div,"										// 登録区分（C：登録、U：更新）
					+ " create_date,"										// 登録年月日
					+ " create_id,"											// 登録者ID
					+ " update_date,"										// 更新年月日
					+ " update_id,"											// 更新者ID
					+ " note)"												// 備考
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			jdbc.update(sql, insertUserInfoDao.getFacCd()					// 工場CD
								,insertUserInfoDao.getAffilicateId()		// 所属ID
								,insertUserInfoDao.getUserId()				// ユーザーID
								,insertUserInfoDao.getUserName()			// ユーザー名
								,insertUserInfoDao.getPass()				// パスワード
								,insertUserInfoDao.getHashPass()			// パスワード（暗号化）
								,insertUserInfoDao.getExpireDateFrom()		// 適用日（FROM）
								,insertUserInfoDao.getExpireDateTo()		// 適用日（TO）
								,insertUserInfoDao.getAuthDiv()				// 権限区分(ゲスト：1、一般：2、管理者：3)
								,insertUserInfoDao.isWatchAuthFlg()			// 参照権限フラグ（権限あり：true、権限なし：false）
								,insertUserInfoDao.isOprAuthFlg()			// 操作権限フラグ（権限あり：true、権限なし：false）
								,"0"										// 削除フラグ
								,"C"										// 登録区分（C：登録、U：更新）
								,insertUserInfoDao.getSystemDate()			// 登録年月日
								,insertUserInfoDao.getLoginUserId()			// 登録者ID
								,insertUserInfoDao.getSystemDate()			// 更新年月日
								,insertUserInfoDao.getLoginUserId()			// 更新者ID
								,null);										// 備考
			
		} catch(DuplicateKeyException e) {
			// 一意制約違反が発生
			System.out.println("リポジトリー：userInfoRegisterメソッド：DuplicateKeyException発生");
			throw e;
			
		} catch(Exception e) {
			System.out.println("リポジトリー：userInfoRegisterメソッド：Exception発生");
			e.printStackTrace();
			
		}
		
		System.out.println("リポジトリー：userInfoRegisterメソッド終了");
		
	}
	
	/**
	 * 更新
	 * @param insertUserInfoDao
	 * @throws OptimisticLockingFailureException
	 */
	public int userInfoUpdate(InsertUserInfoDao insertUserInfoDao) throws OptimisticLockingFailureException {
		
		System.out.println("リポジトリー：userInfoUpdateメソッド開始");
		
		// 更新結果の戻り値用の変数を設定
		int rowNumber = 0;
		
		// 1件更新
		try {
			String sql = "UPDATE user_info SET pass = ?,"						// パスワード
						+ "  hash_pass = ?,"									// パスワード（暗号化）
						+ "  expire_date_to = ?,"								// 適用日（TO）
						+ "  auth_div = ?,"										// 権限区分(ゲスト：1、一般：2、管理者：3)
						+ "  watch_auth_flg = ?,"								// 参照権限フラグ（権限あり：true、権限なし：false）
						+ "  opr_auth_flg = ?,"									// 操作権限フラグ（権限あり：true、権限なし：false）
						+ "  create_div = ?,"									// 登録区分（C：登録、U：更新）
						+ "  update_date = ?,"									// 更新年月日
						+ "  update_id = ?"										// 更新者ID
						+ " WHERE fac_cd = ?"									// 工場CD
						+ "  AND affilicate_id = ?"								// 所属ID
						+ "  AND user_id = ?;";									// ユーザーID
			
			rowNumber = jdbc.update(sql, insertUserInfoDao.getPass()			// パスワード
										,insertUserInfoDao.getHashPass()		// パスワード（暗号化）
										,insertUserInfoDao.getExpireDateTo()	// 適用日（TO）
										,insertUserInfoDao.getAuthDiv()			// 権限区分(ゲスト：1、一般：2、管理者：3)
										,insertUserInfoDao.isWatchAuthFlg()		// 参照権限フラグ（権限あり：true、権限なし：false）
										,insertUserInfoDao.isOprAuthFlg()		// 操作権限フラグ（権限あり：true、権限なし：false）
										,"U"									// 登録区分（C：登録、U：更新）
										,insertUserInfoDao.getSystemDate()		// 更新年月日
										,insertUserInfoDao.getLoginUserId()		// 更新者ID
										,insertUserInfoDao.getFacCd()			// 工場CD
										,insertUserInfoDao.getAffilicateId()	// 所属ID
										,insertUserInfoDao.getUserId());		// ユーザーID
					
		} catch(OptimisticLockingFailureException e) {
			// 排他処理の例外が発生
			System.out.println("リポジトリー：userInfoUpdateメソッド：OptimisticLockingFailureException発生");
			throw e;
			
		} catch(Exception e) {
			System.out.println("リポジトリー：userInfoUpdateメソッド：Exception発生");
			e.printStackTrace();
			
		}
		
		System.out.println("リポジトリー：userInfoUpdateメソッド終了");
		
		return rowNumber;
		
	}
	
}
