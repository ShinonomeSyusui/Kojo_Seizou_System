package com.seizou.kojo.domain.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.seizou.kojo.domain.dao.ProductInfoDao;
import com.seizou.kojo.domain.dao.UserInfoDao;
import com.seizou.kojo.domain.dto.CommonDto;
import com.seizou.kojo.domain.dto.ProductInfoDto;
import com.seizou.kojo.domain.form.ComponentInfoForm;
import com.seizou.kojo.domain.form.ProductInfoForm;
import com.seizou.kojo.domain.repository.Bfmk05Repository;

/**
* 製品・部材情報 Service
* @author K.Tomonari
*/
@Transactional
@Service
public class Bfmk05Service {

    @Autowired
    Bfmk05Repository repo;

    /**
     * 初期画面
     * @param commonDto
     * @param itemCd
     * @return ProductInfoDto
     */
    public ProductInfoDto init(CommonDto commonDto, String itemCd) {
        
    	// 出力用の変数
    	ProductInfoDto productInfoDto = new ProductInfoDto();
    	
    	// 権限区分検索用変数
    	UserInfoDao userInfoDao = new UserInfoDao();
    	
    	userInfoDao.setAffilicateId(commonDto.getAffId()); // 所属ID
    	userInfoDao.setUserId(commonDto.getUserId());      // ユーザーID
    	
    	// 権限区分を実行
        userInfoDao = repo.authDivSearch(userInfoDao);
        
        // ユーザーが受付部・製造部・在庫管理部でゲスト以外の場合
        if(commonDto.getAffId().startsWith("su") 
    		|| commonDto.getAffId().startsWith("cr")
    		|| commonDto.getAffId().startsWith("zk")
    		&& !(userInfoDao.getAuthDiv().get(0).equals("1"))) {	
        	
			// 遷移元が製品一覧画面の場合
			if(commonDto.getTmpDispId().equals("bfmk06")) {
        	    // 入力用の変数
				ProductInfoDao productInfoDao = new ProductInfoDao();
				productInfoDao.setFacCd(commonDto.getFacCd());        // 工場CD
				productInfoDao.setItemCd(itemCd);                     // 品番
    	
        		// 製品情報取得を実行
        		productInfoDao = repo.productInfoSearch(productInfoDao);
        	
        		productInfoDto.setItemCd(productInfoDao.getItemCd());       // 品番
        		productInfoDto.setItemName(productInfoDao.getItemName());   // 品名
        		productInfoDto.setItemNameR(productInfoDao.getItemNameR()); // 品名略称
        		productInfoDto.setItemDiv(productInfoDao.getItemDiv());     // 品名区分
        		productInfoDto.setExpireDateFrom(
        			productInfoDao.getExpireDateFrom());                    // 有効期限日（FROM）
        		productInfoDto.setExpireDateTo(
        			productInfoDao.getExpireDateTo());                      // 有効期限日（TO）
        		productInfoDto.setConstruction(
        			productInfoDao.getConstruction());                      // 工期
        		productInfoDto.setMaterialId(
        				productInfoDao.getMaterialId());                    // 部材ID
        		List<String> reqQty = new ArrayList<String>();
        		for(BigDecimal big : productInfoDao.getReqQty()) {
        			reqQty.add(big.toString());
        		}
        		productInfoDto.setReqQty(reqQty);                           // 必要数
        		productInfoDto.setUnit(productInfoDao.getUnit());           // 単位
			}
        } else {
        	productInfoDto.setMessage("mszemd001");   // 参照・更新権限エラーメッセージ
        }
        
        return productInfoDto;
    }
    
    /**
     * 登録
     * @param productInfoForm
     * @return ProductInfoDto
     */
    public ProductInfoDto register(ProductInfoForm productInfoForm) {
    	// 入力チェック
    	ProductInfoDto productInfoDto = checkValidate(productInfoForm);
    	
    	// メッセージが空の場合
    	if(productInfoDto.getMessage() == null) {
    		ProductInfoDao productInfoDao = new ProductInfoDao();
    		productInfoDao.setFacCd("bfm1");                               // 工場CD
    		productInfoDao.setItemCd(productInfoForm.getItemCd());         // 品番
    		productInfoDao.setItemName(productInfoForm.getItemName());     // 品名
    		// 品名略称が空白の場合
    		if(productInfoForm.getItemDiv().equals("1") 
        		|| productInfoForm.getItemDiv().equals("2")
        		&& StringUtils.isEmpty(productInfoForm.getItemNameR())) {
    			productInfoDao.setItemNameR(productInfoForm.getItemName());    // 品名略称
    		} else {
    			productInfoDao.setItemNameR(productInfoForm.getItemNameR());   // 品名略称
    		}
    		productInfoDao.setItemDiv(productInfoForm.getItemDiv());           // 品名区分
    		productInfoDao.setExpireDateFrom(
    				productInfoForm.getExpireDateFrom());                      // 有効期限日（FROM）
    		
    		if(productInfoForm.getExpireDateTo().isEmpty()) {
    			productInfoDao.setExpireDateTo(null);
    		} else {
    			productInfoDao.setExpireDateTo(
        				productInfoForm.getExpireDateTo());                     // 有効期限日（TO）
    		}
    		if(productInfoForm.getConstruction() != null) {
    			productInfoDao.setConstruction(
    					productInfoForm.getConstruction());                     // 工期
    		}
    	
    		if(productInfoForm.getComponentList() == null) {
    			productInfoDao.setOprConQty(null);  
    		} else {
    			productInfoDao.setOprConQty(
    				productInfoForm.getComponentList().size());                 // 作業工程数
    		}
    		
    		// 製品区分が製品または特注品の場合
    		if(productInfoForm.getItemDiv().equals("1") 
    			|| productInfoForm.getItemDiv().equals("2") ) {
    			
    			// 部材情報を保存する変数
    			List<String> manuId = new ArrayList<String>();
    			List<String> materialId = new ArrayList<String>();
    			List<BigDecimal> reqQty = new ArrayList<BigDecimal>();
    			List<String> unit = new ArrayList<String>();
    			
    			// 部材情報を追加
    			for(ComponentInfoForm com : productInfoForm.getComponentList()) {
    				// 部材ID・必要数・単位が空白の場合
    				if(StringUtils.isEmpty(com.getMaterialId())
    					&& StringUtils.isEmpty(com.getReqQty())
    					&& StringUtils.isEmpty(com.getUnit())) {
    					continue;
    				}
    				manuId.add(productInfoForm.getItemCd());
    				materialId.add(com.getMaterialId());
    				reqQty.add(new BigDecimal(com.getReqQty()));
    				unit.add(com.getUnit());
    			}
    		
    			productInfoDao.setManuId(manuId);                              // 製品ID
    			productInfoDao.setMaterialId(materialId);                      // 部材ID
    			productInfoDao.setReqQty(reqQty);                              // 数量
    			productInfoDao.setUnit(unit);                                  // 単位
    		}
    		productInfoDao.setUserId("hkgt000");                               // ユーザーID
    		
    		// 登録
    		productInfoDao = repo.register(productInfoDao);
   
    		// メッセージが空の場合
    		if(productInfoDao.getMessage() == null) {
    			productInfoDto.setMessage("mszemd016");                        // 登録処理完了メッセージ
    		} else {
    			productInfoDto.setMessage(productInfoDao.getMessage());
    		}
    	}
    
    	return productInfoDto;
    }
    
    /**
     * 更新
     * @param productInfoForm
     * @return ProductInfoDto
     */
    public ProductInfoDto update(ProductInfoForm productInfoForm) {
    	// 入力チェック
    	ProductInfoDto productInfoDto = checkValidate(productInfoForm);
    	
    	// メッセージが空の場合
    	if(productInfoDto.getMessage() == null) {
    		ProductInfoDao productInfoDao = new ProductInfoDao();
    		productInfoDao.setFacCd("bfm1");                               // 工場CD
    		productInfoDao.setItemCd(productInfoForm.getItemCd());         // 品番
    		productInfoDao.setItemNameR(productInfoForm.getItemNameR());   // 品名略称
    		
    		if(productInfoForm.getExpireDateTo().isEmpty()) {
    			productInfoDao.setExpireDateTo(null);
    		} else {
    			productInfoDao.setExpireDateTo(
        				productInfoForm.getExpireDateTo());                 // 有効期限日（TO）
    		}
    		productInfoDao.setConstruction(
    				productInfoForm.getConstruction());                     // 工期
    		
    		// 製品区分が製品または特注品の場合
    		if(productInfoForm.getItemDiv().equals("1") 
    			|| productInfoForm.getItemDiv().equals("2") ) {
    			
    			// 部材情報を保存する変数
    			List<String> manuId = new ArrayList<String>();
    			List<String> materialId = new ArrayList<String>();
    			List<BigDecimal> reqQty = new ArrayList<BigDecimal>();
    			List<String> unit = new ArrayList<String>();
    			// 部材情報を追加
    			for(ComponentInfoForm com : productInfoForm.getComponentList()) {
    				// 部材ID・必要数・単位が空白の場合
    				if(StringUtils.isEmpty(com.getMaterialId())
    					&& StringUtils.isEmpty(com.getReqQty())
    					&& StringUtils.isEmpty(com.getUnit())) {
    					continue;
    				}
    				manuId.add(productInfoForm.getItemCd());
    				materialId.add(com.getMaterialId());
    				reqQty.add(new BigDecimal(com.getReqQty()));
    				unit.add(com.getUnit());
    			}
    		
    			productInfoDao.setManuId(manuId);                              // 製品ID
    			productInfoDao.setMaterialId(materialId);                      // 部材ID
    			productInfoDao.setReqQty(reqQty);                              // 数量
    			productInfoDao.setUnit(unit);                                  // 単位
    		}
    		
    		productInfoDao.setUserId("hkgt000");                               // ユーザーID
    		// 更新
    		productInfoDao = repo.update(productInfoDao);
    		
    		// メッセージが空の場合
    		if(productInfoDao.getMessage() == null) {
    			productInfoDto.setMessage("mszemd017");                        // 更新処理完了メッセージ
    		} else {
    			productInfoDto.setMessage(productInfoDao.getMessage());
    		}
    	}
    	
    	return productInfoDto;
    }
    
    /**
     * 入力チェック
     * @param productInfoForm
     * @return ProductInfoDto
     */
    public ProductInfoDto checkValidate(ProductInfoForm productInfoForm) {
    	ProductInfoDto productInfoDto = new ProductInfoDto();
       
    	// 品番が空白の場合
    	if(StringUtils.isEmpty(productInfoForm.getItemCd())) {
    		productInfoDto.setMessage("mszemd002");
    		return productInfoDto;
    	}
    	// 品名が空白の場合
    	if(StringUtils.isEmpty(productInfoForm.getItemName())) {
    		productInfoDto.setMessage("mszemd003");
    		return productInfoDto;
    	}
    	// 有効期限日（FROM）が空白の場合
    	if(StringUtils.isEmpty(productInfoForm.getExpireDateFrom())) {
    		productInfoDto.setMessage("mszemd005");
    		return productInfoDto;
    	}
    	
    	// 有効期限日（FROM）と有効期限日（TO）が入力されている場合
    	if(!(StringUtils.isEmpty(productInfoForm.getExpireDateFrom())
    	   || StringUtils.isEmpty(productInfoForm.getExpireDateTo()))) {
        	//StringからDateに変換に利用
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            try {
				Date expireDateFromDate = dateFormat.parse(
						productInfoForm.getExpireDateFrom()); // 有効期限日（FROM）
				Date expireDateToDate = dateFormat.parse(
						productInfoForm.getExpireDateTo());   // 有効期限日（TO）
				
				// 有効期限日（FROM）が有効期限日（TO）より未来日の場合
				if(expireDateFromDate.after(expireDateToDate)) {
					productInfoDto.setMessage("mszemd014");
					return productInfoDto;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} 
    	}
    	
    	// 製品区分が部材の場合
    	if(productInfoForm.getItemDiv().equals("3") 
    		&& productInfoForm.getItemCd() != null) {
        	// 品番の頭文字が「BZ」以外の場合
    		if(!(productInfoForm.getItemCd().startsWith("BZ"))) {
    			productInfoDto.setMessage("mszemd007");
    			return productInfoDto;
    		}
        	// 品番が8文字以外の場合
        	if(!(productInfoForm.getItemCd().length() == 8)) {
        		productInfoDto.setMessage("mszemd009");
        		return productInfoDto;
        	}
    	}
    	// 製品区分が製品または特注品の場合
    	if(productInfoForm.getItemDiv().equals("1") 
    		|| productInfoForm.getItemDiv().equals("2") 
    		&& productInfoForm.getItemCd() != null) {
        	// 工期が空白の場合
        	if(productInfoForm.getConstruction() == null) {
        		productInfoDto.setMessage("mszemd004");
        		return productInfoDto;
        	}
        	// 品番の頭文字が「KD」または「RF」以外の場合
        	if(!(productInfoForm.getItemCd().startsWith("KD")
        		|| productInfoForm.getItemCd().startsWith("RF"))) {
        		productInfoDto.setMessage("mszemd006");
        	}
        	// 品番が8～10文字以外の場合
        	if(!(productInfoForm.getItemCd().length() == 8 
        		|| productInfoForm.getItemCd().length() == 9
        		|| productInfoForm.getItemCd().length() == 10)) {
        		productInfoDto.setMessage("mszemd008");
        		return productInfoDto;
        	}
        	// 品番の重複チェック用変数
        	List<String> materialIdList = new ArrayList<String>();
        	for(ComponentInfoForm comForm : productInfoForm.getComponentList()) {
        		
        		// 部材ID・必要数・単位が空白の場合
        		if(StringUtils.isEmpty(comForm.getMaterialId())
        			&& StringUtils.isEmpty(comForm.getReqQty())
        			&& StringUtils.isEmpty(comForm.getUnit())) {
        			continue;
        		}
        		// 部材IDが空白の場合
        		if(StringUtils.isEmpty(comForm.getMaterialId())) {
        			productInfoDto.setMessage("mszemd010");
        			return productInfoDto;
        		}
        		// 必要数が空白の場合
        		if(StringUtils.isEmpty(comForm.getReqQty())) {
        			productInfoDto.setMessage("mszemd011");
        			return productInfoDto;
        		}
        		// 単位が空白の場合
        		if(StringUtils.isEmpty(comForm.getUnit())) {
        			productInfoDto.setMessage("mszemd012");
        			return productInfoDto;
        		}
        		// 品番を追加
        		materialIdList.add(comForm.getMaterialId());
        	}
        	// 部材IDが重複した場合
        	if(materialIdList.size() != materialIdList.stream().distinct().count()) {
        		productInfoDto.setMessage("mszemd013");
        		return productInfoDto;
        	}
    	}
    	
    	return productInfoDto;
    }
}