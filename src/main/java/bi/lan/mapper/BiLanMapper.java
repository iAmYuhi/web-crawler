package bi.lan.mapper;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import bi.lan.model.Feixiaohao;
import bi.lan.model.Jinse;
import bi.lan.model.Walian;

/**
* @author yuhi
* @date 2018年2月2日 下午4:19:35
*/
public interface BiLanMapper {
	
	void batchInsertJinse(@Param("list") List<Jinse> list);
	Timestamp getJinseMaxTime();
	void updateJinse(Jinse jinse);
	List<Integer> getExistIds(@Param("list") List<Integer> ids);
	
	List<String> getFeixiaohaoExistIds(@Param("list") List<String> urls);
	void batchInsertFeixiaohao(@Param("list") List<Feixiaohao> list);
	void updateFeixiaohao(Feixiaohao model);
	
	void batchInsertWailian(@Param("list") List<Walian> list);
	Timestamp getWalanMaxTime();
	List<Integer> getWalianExistIds(@Param("list") List<Integer> ids);
	
}
