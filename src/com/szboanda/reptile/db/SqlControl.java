package com.szboanda.reptile.db;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.szboanda.reptile.modal.PollutionPollutionInfo;
import com.szboanda.reptile.util.Property;

public class SqlControl {
	//批量删除
	public static void deleteData(){
		
		System.out.println();
	}
	
	
	/**
	 * 保存排污许可证数据，这里用许可证号作为条件。如果存在便更新数据库，如果不存在便插入
	 * 
	 */
	public static void save(PollutionPollutionInfo ppi){
		if(ppi != null && ppi.getXkzbh()!=null){
			Connection conn = null;
			try {
				conn = SqlServerDB.getConn();
				QueryRunner qr = new QueryRunner();
				Integer c = qr.query(conn, "select count(*) from T_POLLUTION_INFO where xkzbh = ?",new ScalarHandler<Integer>(),ppi.getXkzbh());
				if(c > 0){//更新
					qr.update(conn, "UPDATE T_POLLUTION_INFO set gsm=?, ywlx=?, bb=?, bjrq=?, yxqx=?, zywrwlb=?, dqzywrwzl=?, dqwrwpfgl=?, dqwrwpfzxbz=?, fszywrwzl=?, fswrwpfgl=?, fswrwpfzxbz=?, pwqshjyxx=?, xkzzb=?, xkzfb=?, dq=? where xkzbh = ?",
							 new Object[]{ppi.getGsm(),ppi.getYwlx(),ppi.getBb(),ppi.getBjrq(),ppi.getYxqx(),ppi.getZywrwlb(),
									 ppi.getDqzywrwzl(),ppi.getDqwrwpfgl(),ppi.getDqwrwpfzxbz(),ppi.getFszywrwzl(),ppi.getFswrwpfgl(),ppi.getFswrwpfzxbz(),ppi.getPwqshjyxx(),ppi.getXkzzb(),ppi.getXkzfb(),ppi.getDq(),ppi.getXkzbh()});
							 
				}else{ //插入
					if(ppi.getXh()==null){
						ppi.setXh(UUID.randomUUID().toString().replace("-", "").toLowerCase());
					}
					qr.update(conn, "insert into T_POLLUTION_INFO (gsm, xkzbh, ywlx, bb,bjrq,yxqx,zywrwlb,dqzywrwzl,dqwrwpfgl,dqwrwpfzxbz,fszywrwzl,fswrwpfgl,fswrwpfzxbz,pwqshjyxx,xkzzb,xkzfb,xh,dq) "
							+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", ppi.getGsm(),ppi.getXkzbh(),ppi.getYwlx(),ppi.getBb(),ppi.getBjrq(),ppi.getYxqx(),ppi.getZywrwlb(),ppi.getDqzywrwzl(),ppi.getDqwrwpfgl()
							,ppi.getDqwrwpfzxbz(),ppi.getFszywrwzl(),ppi.getFswrwpfgl(),ppi.getFswrwpfzxbz(),ppi.getPwqshjyxx(),ppi.getXkzzb(),ppi.getXkzfb(),ppi.getXh(),ppi.getDq());
				
				}
				System.out.println(c);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				SqlServerDB.close(conn);
			}
		}
		
	}
	
	/** 
	 * 批量插入数据
	 * @param ppis
	 */
	public static void executeUpdate(List<PollutionPollutionInfo> ppis){
		Object[][] params = new Object[ppis.size()][18];
		for(int i=0;i<ppis.size();i++){
			params[i][0] = ppis.get(i).getGsm();
			params[i][1] = ppis.get(i).getXkzbh();
			params[i][2] = ppis.get(i).getYwlx();
			params[i][3] = ppis.get(i).getBb();
			params[i][4] = ppis.get(i).getBjrq();
			params[i][5] = ppis.get(i).getYxqx();
			params[i][6] = ppis.get(i).getZywrwlb();
			params[i][7] = ppis.get(i).getDqzywrwzl();
			params[i][8] = ppis.get(i).getDqwrwpfgl();
			params[i][9] = ppis.get(i).getDqwrwpfzxbz();
			params[i][10] = ppis.get(i).getFszywrwzl();
			params[i][11] = ppis.get(i).getFswrwpfgl();
			params[i][12] = ppis.get(i).getFswrwpfzxbz();
			params[i][13] = ppis.get(i).getPwqshjyxx();
			params[i][14] = ppis.get(i).getXkzzb();
			params[i][15] = ppis.get(i).getXkzfb();
			params[i][16] = i;
			params[i][17] = ppis.get(i).getDq();
		}
		System.out.println(ppis.get(171));
		QueryRunner qr = new QueryRunner();
		try {
			qr.batch(SqlServerDB.getConn(), "insert into T_POLLUtiON_INFO (gsm, xkzbh, ywlx, bb,bjrq,yxqx,zywrwlb,dqzywrwzl,dqwrwpfgl,dqwrwpfzxbz,fszywrwzl,fswrwpfgl,fswrwpfzxbz,pwqshjyxx,xkzzb,xkzfb,xh,dq)"
			        + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", params);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Property().loadProperty();
		PollutionPollutionInfo ppi = new PollutionPollutionInfo();
		ppi.setXkzbh("91430822MA4L61LK6W001P");
		save(ppi);
	}
}
