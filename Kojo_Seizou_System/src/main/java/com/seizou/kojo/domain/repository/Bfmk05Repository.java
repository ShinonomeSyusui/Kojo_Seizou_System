package com.seizou.kojo.domain.repository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.seizou.kojo.domain.dao.ProductInfoDao;
import com.seizou.kojo.domain.dao.UserInfoDao;

/**
* 製品・部材情報 Repository
* @author K.Tomonari
*/
@Repository
@Transactional(rollbackFor = Exception.class)
public class Bfmk05Repository {

    @Autowired
    JdbcTemplate jdbc;

    /**
     * 権限区分検索
     * @param userInfoDao
     * @return UserInfoDao
     * @throws DataAccessException
     */
    public UserInfoDao authDivSearch(UserInfoDao userInfoDao) 
    		throws DataAccessException {

        // ユーザー権限区分の取得
        Map<String, Object> map = jdbc.queryForMap("SELECT auth_div FROM user_info"
                + " WHERE affilicate_id = ?"
        		+ " AND user_id = ?"
        		+ " AND del_flg = '0'", 
        		userInfoDao.getAffilicateId(), // 所属ID
        		userInfoDao.getUserId());      // ユーザーID

        // 取得したデータを結果返却用の変数にセット
    	List<String> authDiv = new ArrayList<String>();
    	authDiv.add((String) map.get("auth_div"));
    	userInfoDao.setAuthDiv(authDiv); // 権限区分

        return userInfoDao;
    }

    /**
     * 製品情報検索
     * @param productInfoDao
     * @return ProductInfoDao
     * @throws DataAccessException
     */
    public ProductInfoDao productInfoSearch(ProductInfoDao productInfoDao) 
    		throws DataAccessException {

        // 製品情報の取得
        Map<String, Object> map = jdbc.queryForMap("SELECT "
        		+ "item_cd, "
        		+ "item_name, "
        		+ "item_name_r, "
        		+ "item_div, "
        		+ "expire_date_from, "
        		+ "expire_date_to, "
        		+ "construction"
        		+ " FROM item"
                + " WHERE fac_cd = ?"
        		+ " AND item_cd = ?"
        		+ " AND del_flg = '0'", 
        		productInfoDao.getFacCd(),   // 工場CD
        		productInfoDao.getItemCd()); // 品番

        //DateからStringに変換に利用
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 取得したデータを結果返却用の変数にセット
        productInfoDao.setItemCd((String) map.get("item_cd"));             // 品番
        productInfoDao.setItemName((String) map.get("item_name"));         // 品名
        productInfoDao.setItemNameR((String) map.get("item_name_r"));      // 品名略称
        productInfoDao.setItemDiv((String) map.get("item_div"));           // 品名区分
        if(map.get("expire_date_from") != null) {
        	productInfoDao.setExpireDateFrom((String) 
    			dateFormat.format(map.get("expire_date_from")));           // 有効期限日（FROM）
        }
    	if(map.get("expire_date_to") != null) {
    		productInfoDao.setExpireDateTo((String) 
    			dateFormat.format(map.get("expire_date_to")));             // 有効期限日（TO）
    	}
    	productInfoDao.setConstruction((Integer) map.get("construction")); // 工期

        // 部材情報の取得
    	List<Map<String, Object>> mapList = jdbc.queryForList("SELECT "
        		+ "material_id, "
        		+ " req_qty, "
        		+ "unit"
        		+ " FROM manu_material"
                + " WHERE fac_cd = ?"
        		+ " AND manu_id = ?"
        		+ " AND del_flg = '0'", 
        		productInfoDao.getFacCd(),   //工場CD
        		productInfoDao.getItemCd()); //製品ID

        // 結果返却用の変数
        List<String> materialId = new ArrayList<>();
        List<BigDecimal> reqQty = new ArrayList<>();
        List<String> unit = new ArrayList<>();

        // 取得したデータを結果返却用のListに格納
        for (Map<String, Object> list : mapList) {
        	materialId.add((String) list.get("material_id"));  // 部材ID
        	reqQty.add((BigDecimal) list.get("req_qty"));      // 必要数
        	unit.add((String) list.get("unit"));               // 単位
        }

        productInfoDao.setMaterialId(materialId);              // 部材ID
        productInfoDao.setReqQty(reqQty);                      // 必要数
        productInfoDao.setUnit(unit);                          // 単位

        return productInfoDao;
    }

    /**
     * 製品情報登録
     * @param productInfoDao
     * @return ProductInfoDao
     */
    public ProductInfoDao register(ProductInfoDao productInfoDao) {
        
    	// 品名マスタに製品情報追加
		try {
			String sql = "INSERT INTO item(fac_cd,"		         // 工場CD
					+ " item_cd,"							     // 品番
					+ " item_name,"							     // 品名
					+ " item_name_r,"						     // 品名略称
					+ " item_div,"							     // 品名区分
					+ " expire_date_from,"					     // 有効期限日（FROM）
					+ " expire_date_to,"					     // 有効期限日（TO）
					+ " construction,"						     // 工期
					+ " opr_con_qty,"						     // 作業工程数
					+ " del_flg,"							     // 削除フラグ
					+ " create_div,"						     // 登録区分
					+ " create_date,"						     // 登録年月日
					+ " create_id,"								 // 登録者ID
					+ " update_date,"					         // 更新年月日
					+ " update_id,"							     // 更新者ID
					+ " note)"								     // 備考
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			jdbc.update(sql, productInfoDao.getFacCd()			 // 工場CD
						   , productInfoDao.getItemCd()		     // 品番
						   , productInfoDao.getItemName()		 // 品名
						   , productInfoDao.getItemNameR()		 // 品名略称
						   , productInfoDao.getItemDiv()	     // 品名区分
						   , productInfoDao.getExpireDateFrom()	 // 有効期限日（FROM）
						   , productInfoDao.getExpireDateTo()	 // 有効期限日（TO）
						   , productInfoDao.getConstruction()	 // 工期
						   , productInfoDao.getOprConQty()		 // 作業工程数
						   , "0"			                     // 削除フラグ
						   , "C"		 	                     // 登録区分
						   , LocalDateTime.now()              	 // 登録年月日
						   , productInfoDao.getUserId()			 // 登録者ID
						   , LocalDateTime.now()                 // 更新年月日
						   , productInfoDao.getUserId()			 // 更新者ID
						   , null);								 // 備考

			// 製品関連部材マスタに部材情報追加
			sql = "INSERT INTO manu_material(fac_cd,"	         // 工場CD
					+ " manu_id,"							     // 製品ID
					+ " material_id,"							 // 部材ID
					+ " req_qty,"						         // 必要数
					+ " unit,"							         // 単位
					+ " del_flg,"							     // 削除フラグ
					+ " create_div,"						     // 登録区分
					+ " create_date,"						     // 登録年月日
					+ " create_id,"								 // 登録者ID
					+ " update_date,"					         // 更新年月日
					+ " update_id,"							     // 更新者ID
					+ " note)"								     // 備考
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			if(productInfoDao.getManuId() != null) {
				for(int i = 0; i < productInfoDao.getManuId().size(); i++) {
					jdbc.update(sql, productInfoDao.getFacCd()		// 工場CD
							, productInfoDao.getManuId().get(i)		// 製品ID
							, productInfoDao.getMaterialId().get(i) // 部材ID
							, productInfoDao.getReqQty().get(i)	    // 必要数
							, productInfoDao.getUnit().get(i)	    // 単位
							, "0"			                        // 削除フラグ
							, "C"		 	                        // 登録区分
							, LocalDateTime.now()                  	// 登録年月日
							, productInfoDao.getUserId()	        // 登録者ID
							, LocalDateTime.now()                  	// 更新年月日
							, productInfoDao.getUserId()		   	// 更新者ID
							, null);							   	// 備考
				}
			}
		} catch(DuplicateKeyException e) {
			// 一意制約違反が発生
			productInfoDao.setMessage("mszemd015");
			System.out.println(e);
		} catch(Exception e) {
			productInfoDao.setMessage("mszemd018");
			System.out.println(e);
		}

        return productInfoDao;
    }

    /**
     * 製品情報更新
     * @param productInfoDao
     * @return ProductInfoDao
     */
    @Transactional(isolation=Isolation.REPEATABLE_READ)
    public ProductInfoDao update(ProductInfoDao productInfoDao) {
    	try {
    		// 登録区分を'D'に更新
    		jdbc.update("UPDATE item"
    				+ " SET"
    				+ " item_name_r = ?,"
    				+ " expire_date_to = ?,"
    				+ " construction = ?,"
    				+ " create_div = 'U',"
    				+ " update_date = ?,"
    				+ " update_id = ?"
    				+ " WHERE fac_cd = ?"
    				+ " AND item_cd = ?"
    				+ " AND del_flg = '0'", 
    				productInfoDao.getItemNameR(),      // 品名略称
    				productInfoDao.getExpireDateTo(),   // 有効期限日（TO）
    				productInfoDao.getConstruction(),	// 工期
    				LocalDateTime.now(),          		// 更新年月日
    				productInfoDao.getUserId(),    		// 更新者ID
    				productInfoDao.getFacCd(),     		// 工場CD
    				productInfoDao.getItemCd());   		// 品番]

    		// 製品IDが空でない場合
    		if(productInfoDao.getManuId() != null) {

    	        // 登録者と登録年月日を取得
    	    	List<Map<String, Object>> mapList = jdbc.queryForList("SELECT "
    	        		+ "create_date, "                   // 登録年月日
    	        		+ "create_id"                       // 登録者ID
    	        		+ " FROM manu_material"
    	                + " WHERE fac_cd = ?"               // 工場CD
    	        		+ " AND manu_id = ?"                // 製品ID
    	        		+ " AND del_flg = '0'",             // 削除フラグ
    	        		productInfoDao.getFacCd(),          // 工場CD
    	        		productInfoDao.getManuId().get(0)); // 製品ID

    	        //  登録者と登録年月日の変数
    	        List<LocalDateTime> createDate = new ArrayList<>();
    	        List<String> createId = new ArrayList<>();

    	        // 取得したデータを結果返却用のListに格納していく
    	        for (Map<String, Object> list : mapList) {
    	        	createDate.add((LocalDateTime) list.get("create_date"));  // 登録年月日
    	        	createId.add((String) list.get("create_id"));             // 登録者ID
    	        }

    			// 部材情報を削除
    			jdbc.update("DELETE FROM manu_material"
    				+ " WHERE fac_cd = ?"
    				+ " AND manu_id = ? "
    				+ " AND del_flg = '0'", 
    				productInfoDao.getFacCd(),            // 工場CD
    				productInfoDao.getManuId().get(0));   // 製品ID

    			// 製品関連部材マスタに部材情報追加
				String sql = "INSERT INTO manu_material(fac_cd," // 工場CD
					+ " manu_id,"							     // 製品ID
					+ " material_id,"							 // 部材ID
					+ " req_qty,"						         // 必要数
					+ " unit,"							         // 単位
					+ " del_flg,"							     // 削除フラグ
					+ " create_div,"						     // 登録区分
					+ " create_date,"						     // 登録年月日
					+ " create_id,"								 // 登録者ID
					+ " update_date,"					         // 更新年月日
					+ " update_id,"							     // 更新者ID
					+ " note)"								     // 備考
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

				for(int i = 0; i < productInfoDao.getManuId().size(); i++) {

					jdbc.update(sql, productInfoDao.getFacCd()		    // 工場CD
						       , productInfoDao.getManuId().get(i)		// 製品ID
						       , productInfoDao.getMaterialId().get(i)  // 部材ID
						       , productInfoDao.getReqQty().get(i)	    // 必要数
						       , productInfoDao.getUnit().get(i)	    // 単位
						       , "0"			                        // 削除フラグ
						       , "U"		 	                        // 登録区分
						       , createDate.size() > i ? createDate.get(i) 
						    		   : LocalDateTime.now()            // 登録年月日
						       , createId.size() > i ? createId.get(i)
						    		   : productInfoDao.getUserId()     // 登録者ID
						       , LocalDateTime.now()                  	// 更新年月日
						       , productInfoDao.getUserId()		        // 更新者ID
						       , null);							        // 備考
				}
    		}
    	} catch(OptimisticLockingFailureException e) {
    		// 排他処理の例外発生
			productInfoDao.setMessage("mszemd018");
			System.out.println(e);
    	} catch(Exception e) {
			productInfoDao.setMessage("mszemd018");
			System.out.println(e);
		}

        return productInfoDao;
    }
}