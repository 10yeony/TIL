package broker.three.client;
/*
 * Protocol + Jury
 * 서버----ProtocolHandler
 * 클라이언트 --- Broker
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.transaction.InvalidTransactionException;

import broker.three.exception.DuplicateSSNException;
import broker.three.exception.RecordNotFoundException;
import broker.three.shares.Command;
import broker.three.vo.CustomerRec;
import broker.three.vo.SharesRec;
import broker.three.vo.StockRec;

//Database의 동명이인
//클라이언트 사이드의 통신의 대표주자...Socket...스트림
public class Protocol {
	
	private Socket s;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Command cmd;
	public static final int MIDDLE_PORT = 60000;	
	
	public Protocol(String serverIp) throws Exception{
		s = new Socket(serverIp, MIDDLE_PORT);
		oos = new ObjectOutputStream(s.getOutputStream());
		ois = new ObjectInputStream(s.getInputStream());		
	}
	
	public void close() {
		try {
			s.close();
		}catch(Exception e) {
			System.out.println("Protocol.close()...."+e);
		}
	}
	
	//공통적인 로직 작성....중복되는 코드 
	public void writeCommand(Command cmd) {
		try {
			oos.writeObject(cmd);
			System.out.println("Client writeCommand....end..");
		}catch(IOException e) {
			System.out.println("Client Protocol writeCommand....error"+e);
		}
	}
	
	public int getResponse() { //readObject() + getResults().getStatus()--> status code
		try {
			cmd =(Command)ois.readObject();
			System.out.println("client readObject()....end...");			
		}catch(Exception e){
			System.out.println("client getResponse()....error"+e);	
		}
		//0, DuplicateE(-2), RecordNE(-1), InvalidTE(-3)
		int status=cmd.getResults().getStatus();
		return status;
	}
	
	public void addCustomer(CustomerRec cust) throws DuplicateSSNException {
		/*
		 * 1. 도시락 싼다.
		 * 2. 던진다...Jury 가 받는다
		 * --------------------------------
		 * 3. Jury다 던진 도시락 다시 받는다
		 * 4. 양수|정상   음수|펑
		 */
		//도시락 싼다
		cmd = new Command(Command.ADDCUSTOMER);
		String[ ] str = {cust.getSsn(), cust.getName(), cust.getAddress()};
		cmd.setArgs(str);
		
		//도시락 날린다.
		writeCommand(cmd);
		
		//Jury가 보낸 데이타 받는다....응답결과
		int status=getResponse();
		if(status==-2) throw new DuplicateSSNException("그런 회원 이미 있어요..");
	}
	public void deleteCustomer(String ssn) throws RecordNotFoundException {
		cmd = new Command(Command.DELETECUSTOMER);
		String[ ] str = {ssn};
		cmd.setArgs(str);
		
		writeCommand(cmd);
		//
		int status=getResponse();
		if(status==-1) throw new RecordNotFoundException("삭제할 대상이 없어요!!");
	}
	
	public void updateCustomer(CustomerRec cust) throws RecordNotFoundException  {
		cmd = new Command(Command.UPDATECUSTOMER);
		String[ ] str = {cust.getSsn(), cust.getName(),cust.getAddress()};
		cmd.setArgs(str);
		
		writeCommand(cmd);
		//
		int status = getResponse();
		if(status==-1)throw new RecordNotFoundException("수정할 대상이 없어요!!");		
	}
	
	public CustomerRec getCustomer(String ssn){
		CustomerRec cr = null;
		//1. 도시락 싼다.
		cmd  = new Command(Command.GETCUSTOMER); 
		String[ ] str = {ssn};
		cmd.setArgs(str);
		
		//2. 도시락 던진다..Jury쪽으로
		writeCommand(cmd);
		
		/////////////////////////////////////////////
		
		//3. Jury 가 다시 던진 도시락 받는다.
		int status=getResponse();//상태값, 결과값  | //cmd는 Jury 가 던진 도시락
		CustomerRec cust = (CustomerRec)cmd.getResults().get(0);
		return cust;
	}
	
	public ArrayList<CustomerRec> getAllCustomers(){
		ArrayList<CustomerRec> list =new ArrayList<>();
		cmd = new Command(Command.GETALLCUSTOMERS);
		
		writeCommand(cmd);
		
		getResponse(); //cmd....jury가 던진 cmd
		list=(ArrayList<CustomerRec>)cmd.getResults().get(0);
		return list;
	}
	
	public ArrayList<StockRec> getAllStocks(){
		ArrayList<StockRec> list = null;
		cmd = new Command(Command.GETALLSTOCK);
		
		writeCommand(cmd);
		getResponse(); //cmd....jury가 던진 cmd
		list=(ArrayList<StockRec>)cmd.getResults().get(0);
		return list;
	}
	
	public void buyShares(String ssn, String symbol, int quantity)  {
		//도시락 싼다...Data Packing..setter
		cmd = new Command(Command.BUYSHARES);
		String[ ] str = {ssn, symbol,String.valueOf(quantity)};
		cmd.setArgs(str);
		
		writeCommand(cmd);
		
		getResponse();//data Unpacking...getter
		
	}
	public void sellShares(String ssn, String symbol, int quantity)throws RecordNotFoundException, InvalidTransactionException  {
		cmd = new Command(Command.SELLSHARES);
		String[ ] str = {ssn, symbol,String.valueOf(quantity)};
		cmd.setArgs(str);
		
		writeCommand(cmd);
		
		int status=getResponse();
		if(status==-1) throw new RecordNotFoundException("주식을 팔 사람이 없어요!!");
		if(status==-3) throw new InvalidTransactionException("팔려는 주식의 양이 넘 많아요!!");
	}
}











