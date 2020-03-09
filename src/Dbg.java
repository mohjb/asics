/**
 * Created by moh on 7/3/20.
 */

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.*;
import java.sql.*;
import java.net.URL;
import java.util.Date;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

public class Dbg {

static final String Name="asics.Dbg";

public static void p(Object...p){for(Object s:p)System.out.print(s);System.out.println();}


public static void main(String[]args)throws Exception{
	Srvlt s= Srvlt.sttc;
	s.pc=new PC();
	s.pc.a= SrvltContxt.sttc();
	s.pc.q.ssn=new Ssn();
	String[][]prms= {/*1stString is method
			, 2ndString is url(class/app/typ/key)
				where typ is optional depending on method
			, 3rdString is body ,and most methods
				take the body as JsonStorage.val*/
			{"POST","/garajatyFinder/index.jsp",
					"{op:\"signup\",user:{username:\"moh\",pw:\n" +
							"\t\"TWpFeU16Sm1NamszWVRVM1lUVmhOelF6T0RrMFlUQmxOR0U0TURGbVl6TT0=\"\n" +
							"\t,email:\"i@i-1.io\",lastModified:{class:\"Date\",time:1234567}}\n" +
							"\t,clientTime:{class:\"Date\",time:1234567}}"
			},
			{"POST","/garajatyFinder/index.jsp", "{\"op\":\"logout\"}" },
			{"POST","/garajatyFinder/index.jsp",
					"{\"op\":\"login\",\"username\":\"admin\",\"pw\":\"" +
							//"WXpNeU9EUmtNR1k1TkRZd05tUmxNV1prTW1GbU1UY3lZV0poTVRWaVpqTT0="+
							//"YzMyODRkMGY5NDYwNmRlMWZkMmFmMTcyYWJhMTViZjM="+
							"TWpFeU16Sm1NamszWVRVM1lUVmhOelF6T0RrMFlUQmxOR0U0TURGbVl6TT0="+
							"\",\"lastModified\":{\"class\":\"Date\",\"time\":1234567}" +
							",\"clientTime\":{\"class\":\"Date\",\"time\":1234567}}"
			},
			{"POST","/garajatyFinder/index.jsp",
					"{op:\"poll\",lastModified:{\"class\":Date,time:1546162000000}\n" +
							"\t,clientTime:{class:\"Date\",time:1546161669488}\n" +
							" }"
			}
	}; // Dbg$main

	for(String[]p:prms){
		s.pc.q.init(p);
		service( s.pc.p,s.pc.q );s.pc.q.ssn.newlySsn=false;
	}}



final static String packageName="asics"
		,jspName="index.jsp"
		, SrvltName = packageName + "."+jspName;

static Prop usrRole( TL tl){
	Object o=tl==null|tl.usr==null?null:tl.usr.get("role");
	OS e=o instanceof OS ?(OS )o:null;
	return e;}

static int usrRoleLevel(TL tl){
	OS r=usrRole(tl);
	Number l=r==null?null:r.propNum("level");
	return l==null?-1:l.intValue();
}


//////////////////////////////////////////////////////////////////////

public static class AsicSrvlt{//GrjSrvlt

	@HttpMethod(usrLoginNeeded = false) public static Map
	login(@HttpMethod(prmName = "username") String un
			, @HttpMethod(prmName = "pw") String pw
			, @HttpMethod(prmName = "lastModified") Date lm
			,@HttpMethod(prmName = "clientTime")Date clientTime
			, TL tl)throws Exception {
		User u=User.load(un);
		Map m=Util.mapCreate("time",tl.now);
		if(u != null && pw != null && u.pw!=null) {
			String b2 = Util.b64d(Util.b64d(pw))//b1=b64d(pw) YzMyODRkMGY5NDYwNmRlMWZkMmFmMTcyYWJhMTViZjM=
					//b2=b64d(b1) c3284d0f94606de1fd2af172aba15bf3
					,m2Pm = Util.md5 (b2)
					,m2Db = Util.b64d(u.pw);//c3284d0f94606de1fd2af172aba15bf3
			if(m2Db.equals(m2Pm)) {
				OS e= OS.load("user",u.id)
						,r=e.fk("role","roleId");
				tl.h.s("usr", tl.usr =Util.mapCreate("osr",e,"user",u,"role",r));
				Util.mapSet(m,"role",r,"user",e);
			}}//m1=md5("admin") 21232f297a57a5a743894a0e4a801fc3
		// m1b1=b64e(m1) MjEyMzJmMjk3YTU3YTVhNzQzODk0YTBlNGE4MDFmYzM=
		// m1b2=b64e(m1b1) TWpFeU16Sm1NamszWVRVM1lUVmhOelF6T0RrMFlUQmxOR0U0TURGbVl6TT0=  // WXpNeU9EUmtNR1k1TkRZd05tUmxNV1prTW1GbU1UY3lZV0poTVRWaVpqTT0
		// , m2= md5(m1):c3284d0f94606de1fd2af172aba15bf3
		// b1m2=b64(m2):YzMyODRkMGY5NDYwNmRlMWZkMmFmMTcyYWJhMTViZjM=
		return m;}

	@HttpMethod public static void logout(TL tl){ tl.usr =null;}

	@HttpMethod public static List<OS >poll(
		@HttpMethod(prmName = "lastModified")Date lastModified,
		@HttpMethod(prmName = "clientTime")Date clientTime,TL tl) {
		Object o=tl.usr==null?null:tl.usr.get("user");
		if(!(o instanceof User) || clientTime==null||lastModified==null)
			return null;
		User u=(User)o;
		OS e=new OS();int ur=usrRoleLevel(tl);
		boolean makeClones=true;
		List<OS >l=new LinkedList<OS >();
		long delta=clientTime.getTime()-lastModified.getTime();
		Date lm=new Date(tl.now.getTime()-delta);
		for(Sql.Tbl t:e.query( e.loadCols()
				, OS.where(Util.lst(
						OS.C.log//lastModified
						,Sql.Tbl.Co.ge)
						,lm)//, OS.C.lastModified,Sql.Tbl.Co.max
				, OS.cols( OS.C.objectStore, OS.C.id, OS.C.log)//lastModified
				,makeClones)
				)try
		{	OS x=(OS )t;
			if(x.checkAccess(u,ur,null,tl))
				l.add(x);
		}catch(Exception ex){
			//tl.error(ex);
		}
		return l;}

	@HttpMethod public static boolean dbSave(
			@HttpMethod(prmName = "lastModified")Date lastModified,
			@HttpMethod(prmName = "clientTime")Date clientTime,
			@HttpMethod(prmName = "objectStore")String et,
			@HttpMethod(prmName = "id")String id,
			@HttpMethod(prmName = "jsonObj")Object obj,TL tl
	)throws Exception {
		OS b=dbSave(lastModified,clientTime,et,id,obj,null,null,tl);
		return b!=null;}

	public static OS dbSave(
			Date lastModified,Date clientTime,
			String os,String id,Object obj,
			User usr,Integer usrRoleLevel,TL tl)throws Exception{
		if(tl.usr==null)return null;
		if(usr==null)usr=(User)tl.usr.get("user");
		Map m=obj instanceof Map?(Map)obj:null;
		if(User.dbtName.equals(os) && m!=null){
			String un=(String)m.get("username")
					,pw=(String)m.get("pw")
					,p2=Util.b64e(Util.md5(Util.b64d(Util.b64d(pw))));
			if(un!=null){
				User u=User.load(un);
				if(u!=null && u.id!=null && ! u.id.equals(id))
					return null;
				if(u!=null)
				{if(!usr.id.equals(id) ){
					if(usrRoleLevel==null)
						usrRoleLevel=usrRoleLevel(tl);
					if(usrRoleLevel<1)
						return null;
				}
					boolean b1=un!=null &&!un.equals(u.username)
							,b2=p2!=null &&!p2.equals(u.pw);
					if(b1||b2){
						if(b1)u.username=un;
						if(b2){u.pw=pw;
							m.put("pw",null);
						}
						u.save();
					}
				}
			}
		}
		if(m!=null){int count=0;String k="id";boolean
				                                      b=m.containsKey(k);if(id==null && b)id=(String)m.get( k );else if(id!=null&& !b){m.put(k,id);count++;}

			b=m.containsKey(k="lastModified");Date v=lastModified;
			if(v==null && m.containsKey(k))lastModified=(Date)m.get( k );else if(v!=null&& !b){m.put(k,v);count++;}

			v=clientTime;
			if(v==null && (b=m.containsKey(k="clientTime")))clientTime=(Date)m.get( k );//else if(v!=null&& !b){m.put(k,v);count++;}
			if( count>0)
				obj=tl.jo().clrSW().out(m);
		}
		if(lastModified==null || clientTime==null)
			return null;
		OS e=new OS();//e.log =new Date();
		e.objectStore =os;
		if(id!=null&& !id.contains(".") && ! User.dbtName.equals( os ))e.id=usr.id+"."+id;
		e.userId=usr.id;
		e.clientTime=clientTime;
		long delta=clientTime.getTime()-lastModified.getTime();
		e.lastModified=new Date(tl.now.getTime()-delta);
		if(e.checkAccess(usr,usrRoleLevel,m,tl)){
			e.json=obj instanceof String?(String)obj:tl.jo().clrSW().out(obj);
			e.save(tl);
			return e;}
		return null;}

	/**
	 * parameters
	 * "clientTime":<int or {"class":"Date","time":<int or str-date>}>
	 * "changes":[
	 *  {"objectStore":<str>
	 *      ,"obj":{"id":<str>
	 *              , "lastModified":<int or obj>
	 *              , <props>*
	 *            }
	 *  }*
	 * ]
	 * */
	@HttpMethod public static Map changes(
			@HttpMethod(prmName = "changes")List l,
			@HttpMethod(prmName = "clientTime")Date clientTime,TL tl){
		if(tl.usr==null|| l==null)
			return null;Map r=new HashMap();
		User usr=(User)tl.usr.get("user");String id=null;
		Integer usrRoleLevel=usrRoleLevel(tl);
		for(Object o:l)if(o instanceof Map)try
		{Map m=(Map)o;//,obj=(Map)m.get("obj");
			Date  lastModified=(Date)m.get("lastModified");
			String e=(String)m.get("objectStore");id=(String)m.get("id");
			Object obj=m.get("obj");
			r.put(id,dbSave(lastModified,clientTime,e,id,obj,usr,usrRoleLevel,tl)
			);id=null;
		}catch(Exception ex){
			tl.error(ex);
			r.put(id,ex);
		}return r;}

}//class GrjSrvlt

//////////////////////////////////////////////////////////////////////

public static class User extends Sql.Tbl{
	public static final String dbtName="user";
	@Override public String getName(){return dbtName;}

	@F public String id,username,pw;

	public enum C implements CI{
		id,username,pw;
		public Field f()
		{
			return Co.f(name(), User.class);
		}

		public String getName()
		{
			return name();
		}

		public Class getType()
		{
			return f().getType();
		}

	}//C

	@Override public  CI[]columns(){ return C.values(); }

	@Override public Object[]wherePK(){Object[]a={C.id,id};return a;}


	@Override public List DBTblCreation( TL tl ) {
		return Util.lst(Util.lst(
				"varchar(255) NOT NULL DEFAULT '0.0' primary key"//id
				,"varchar(255) NOT NULL DEFAULT 'home' "//username
				,"varchar(255)NOT NULL DEFAULT 'home' "//pw
				), Util.lst("unique(`"+C.username+"`)")
				,Util.lst(Util.lst(1,"admin",Util.b64e(//YzMyODRkMGY5NDYwNmRlMWZkMmFmMTcyYWJhMTViZjM=
						Util.md5 //c3284d0f94606de1fd2af172aba15bf3 2nd
								         (Util.md5//21232f297a57a5a743894a0e4a801fc3 1st
										                  ("admin"))))));}

//class-User-declarations:

	public static User load(String un){
		User e=(User)loadWhere(User.class,where(C.username,un));
		return e;}

}//class User

//////////////////////////////////////////////////////////////////////
/**Object Store Entity entry/row*/
public static class Prop extends Sql.Tbl{
	public static final String dbtName="prop";
	@Override public String getName(){return dbtName;}

	@F public Integer id;
	@F public Date /**server-log-time ,the primary-key in the dbTable*/log;
	@F public String usr,domain,page,prop,val;//no,

	public enum C implements CI{
		id,log,usr,domain,page,prop,val;
		public Field f(){return Co.f(name(), Prop.class);}
		public String getName(){return name();}
		public Class getType(){return f().getType();}
	}//C

	@Override public  CI[]columns(){return C.values();}
	@Override public Object[]wherePK(){Object[]a={C.id, id };return a;}

	@Override public List DBTblCreation(TL tl) {
		return Util.lst(Util.lst(
				"integer  NOT NULL primary key auto_increment" //id
				,"timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP " //log
				,"varchar(255) NOT NULL" //usr
				,"varchar(255) NOT NULL" //domain
				,"varchar(255) NOT NULL DEFAULT 'home' " //page
				,"varchar(255) NOT NULL DEFAULT 'home' " //prop
				,"text NOT NULL " //val
				)
				, Util.lst(Util.lst(C.objectStore,C.id,C.log )
						,Util.lst(C.userId,C.lastModified),Util.lst(C.log))
				,Util.lst(
						Util.lst(null,tl.now,tl.now,tl.now,"user","1","1","{\"username\":\"admin\",\"id\":\"1\",\"roleId\":\"1\"}")//insert into `entity` values(now(),now(),now(),'user','1','1','{"username":"admin","id":"1","roleId":"1"}');
						,Util.lst(null,tl.now,tl.now,tl.now,"role","1","1","{\"title\":\"admin\",\"id\":\"1\",\"level\":\"2\"}")// insert into `entity` values(now(),now(),now(),'role','1','1','{"title":"admin","id":"1","level":"2"}');
				)
		);}

	//class-OS-declarations:


	public Map mObj(){
		Object o=jObj();
		return o instanceof Map?(Map)o:null;}

	public Object jObj(){
		Object o=null;try{o=Json.Prsr.parse(json);}catch(Exception ex){}
		return o;}

	public boolean checkAccess(User u,Integer usrRoleLevel,Map m,TL tl)throws IllegalAccessException {
		if(u==null){
			Object o=tl.usr==null?null:tl.usr.get("user");
			u=o instanceof User?(User)o:null;
		}OS x=this;
		if(u!=null&&(!User.
				                  dbtName.equals(x.objectStore )
				             || u.id.equals(x.id)
				             || (usrRoleLevel!=null&&usrRoleLevel>1)))
		{if(usrRoleLevel!=null && usrRoleLevel<1 && "garage".equals(x.objectStore ) ){
			if(m==null) m=x.mObj();
			if(m!=null ){
				Date d=x.propDt(m,"live");
				if(d==null || tl.now.before(d))
					return false;
				d=x.propDt(m,"expiry");
				if(d==null || tl.now.after(d))
					return false;
			}
		}if((usrRoleLevel==null ||usrRoleLevel<1) && User.dbtName.equals(x.objectStore ))
		{	String roleId=x.propStr(m,"roleId");
			OS myRole=usrRole(tl);
			String myRId=myRole==null?null:myRole.id;
			if(myRole==null || usrRoleLevel==null ||
					   (!myRId.equals(roleId) && usrRoleLevel<1))
				return false;}
			return true;
		}
		throw new IllegalAccessException("ObjectStoreEntity illegal access");
	}//checkAccess

}//class OS

//////////////////////////////////////////////////////////////////////

enum context{ROOT(
		                 "C:\\apache-tomcat-8.0.15\\webapps\\ROOT\\"
		                 ,"/Users/moh/Google Drive/air/apache-tomcat-8.0.30/webapps/ROOT/"
		                 ,"/public_html/i1io/"
		                 ,"D:\\apache-tomcat-8.0.15\\webapps\\ROOT\\"
);
	String str,a[];context(String...p){str=p[0];a=p;}
	enum DB{
		pool("dbpool-"+SrvltName)
		,reqCon("javax.sql.PooledConnection")
		,server("localhost","216.227.216.46")//,"216.227.220.84"
		,dbName("garajatyfinder","js4d00_garajatyfinder")
		,un("root","js4d00_theblue")
		,pw("qwerty","theblue","")
		;String str,a[];DB(String...p){str=p[0];a=p;}
	}

	static String getRealPath(TL t,String path){
		String real=t.h.getServletContext().getRealPath(path);
		boolean b=true;
		try{File f=null;
			if(real==null){int i=0;
				while( i<ROOT.a.length && (b=(f==null|| !f.exists())) )
					try{
						f=new File(ROOT.a[i++]);
					}catch(Exception ex){}//t.error
				real=(b?"./":f.getCanonicalPath())+path;
			}
		}catch(Exception ex){
			t.error(ex,SrvltName,".context.getRealPath:",path);
		}
		return real==null?"./"+path:real;}

	static int getContextIndex(TL t){
		try{File f=null;
			int i=ROOT.a.length-1;
			while( i>=0 )
			{	f=new File(ROOT.a[i]);
				if(f!=null && f.exists())
					return i;i--;
			}
		}catch(Exception ex){
			t.error(ex,SrvltName,".context.getContextIndex:");
		}
		return -1;}

	//***/static Map<Sql,String> getContextPack(TL t,List<Map<Sql,String>>a){return null;}
}//context

//////////////////////////////////////////////////////////////////////

static void staticInit() {
	registerMethods(GrjSrvlt.class);
	registerMethods(User.class);
	if(! Sql.Tbl.registered.contains(User.class))
		Sql.Tbl.registered.add(User.class);
	registerMethods(OS.class);
	if(! Sql.Tbl.registered.contains(OS.class))
		Sql.Tbl.registered.add(OS.class);
}

static Map<String, Method> mth = new HashMap<String, Method>();

static {staticInit();}

public static void registerMethods(Class p) {
	Method[] b = p.getMethods();
	String cn = p.getSimpleName();
	for(Method m : b) {
		HttpMethod h = m.getAnnotation(HttpMethod.class);
		if(h != null) {
			String s = m.getName();
			mth.put(h.useClassName() ? cn + "." + s : s, m);
		}
	}
}//registerHttpMethod

public static void service(HttpServletResponse response,HttpServletRequest request) {
	TL tl = null;
	Object retVal = null;
	try {
		tl = TL.Enter(request, response);
		tl.h.r("contentType", "text/json");
		String hm = tl.h.req("op");if(hm==null)tl.h.req.getMethod();
		Method op = mth.get(hm);
		if(op == null)
			for(String s : mth.keySet())
				if(s.equalsIgnoreCase(hm))
					op = mth.get(s);
		HttpMethod httpMethodAnno = op == null ? null : op.getAnnotation(HttpMethod.class);
		tl.log("_jsp:version2017.02.09.17.10:op=", op, httpMethodAnno);
		if(tl.usr == null && (httpMethodAnno == null || httpMethodAnno.usrLoginNeeded()))
			op = null;
		if(op != null) {
			Class[] prmTypes = op.getParameterTypes();
			Class cl = op.getDeclaringClass();
			Annotation[][] prmsAnno = op.getParameterAnnotations();
			int n = prmsAnno == null ? 0 : prmsAnno.length, i = - 1;tl.h.urli=-1;
			Object[] args = new Object[n];

			for(Annotation[] t : prmsAnno) try {
				HttpMethod pp = t.length > 0 && t[0] instanceof HttpMethod ? (HttpMethod) t[0] : null;
				Class prmClss = prmTypes[++ i];
				String nm = pp != null ? pp.prmName() : "arg" + i;//t.getName();
				Object o = null;
				if(pp != null && pp.prmUrlPart()) {
					args[i]=tl.h.url[tl.h.urli++];
				} else if(pp != null && pp.prmUrlRemaining()) {
					if(tl.h.url!=null&&tl.h.urli<tl.h.url.length) {
						StringBuilder b = new StringBuilder(tl.h.url[tl.h.urli++]);
						while(tl.h.urli<tl.h.url.length){b.append('/' ).append(tl.h.url[tl.h.urli++]);
						}
						args[i] = b.toString();//url.indexOf(urlIndx + 1);
					}
				} else if(pp != null && pp.prmLoadByUrl()) {
					Class[] ca = {TL.class , String.class};
					Method//m=cl.getMethod( "prmLoadByUrl", ca );if(m==null)
							m = prmClss.getMethod("prmLoadByUrl", ca);
					args[i] = m == null ? null : m.invoke(prmClss, tl,tl.h.url);
				} else if(Sql.Tbl.class.isAssignableFrom(prmClss)) {
					Sql.Tbl f = (Sql.Tbl) prmClss.newInstance();
					args[i] = f;
					if(pp != null && pp.prmBody())
						f.fromMap(tl.json);
					else {
						o = tl.json.get(nm);
						if(o instanceof Map) f.fromMap((Map) o);
						else if(o instanceof List) f.vals(((List) o).toArray());
						else if(o instanceof Object[]) f.vals((Object[]) o);
						else f.readReq("");}
				}
				else if(pp != null && pp.prmBody())
					args[i] = prmClss.isAssignableFrom(String.class)
							          ? Util.readString(tl.h.req.getReader())
							          : tl.bodyData;
				else
					args[i] = o = TL.class.equals(prmClss) ? tl
							              : tl.h.req(nm, prmClss);
			} catch(Exception ex) {
				tl.error(ex, SrvltName, ".service:arg:i=", i);
			}
			retVal = n == 0 ? op.invoke(cl)
					         : n == 1 ? op.invoke(cl, args[0])
							           : n == 2 ? op.invoke(cl, args[0], args[1])
									             : n == 3 ? op.invoke(cl, args[0], args[1], args[2])
											               : n == 4 ? op.invoke(cl, args[0], args[1], args[2], args[3])
													                 : n == 5 ? op.invoke(cl, args[0], args[1], args[2], args[3], args[4])
															                   : op.invoke(cl, args);
			if(httpMethodAnno != null && httpMethodAnno.nestJsonReq() && tl.json != null) {
				tl.json.put("return", retVal);
				retVal = tl.json;
			}
		}
		// else Util.mapSet(tl.response,"msg","Operation not authorized ,or not applicable","return",false);
		if(tl.h.r("responseDone") == null) {
			if(tl.h.r("responseContentTypeDone") == null)
				response.setContentType(String.valueOf(tl.h.r("contentType")));
			Json.Output o = tl.getOut();
			retVal=Util.mapCreate("time",tl.now , "request",tl.json , "return",retVal);
			o.o(retVal);
			tl.log(SrvltName, ".run:xhr-response:", tl.jo().o(retVal).toString());
		}
		tl.getOut().flush();
	} catch(Exception x) {
		if(tl != null) {
			tl.error(x, SrvltName, ":");
			try {
				tl.getOut().o(x);
			} catch(IOException iox) {
			}
		} else
			x.printStackTrace();
	} finally {
		TL.Exit();
	}
}//run op servlet.service

//////////////////////////////////////////////////////////////////////

/**
 * annotation to designate a java method as an ajax/xhr entry point of execution
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpMethod {
	boolean useClassName() default false;
	boolean nestJsonReq() default true;//if false , then only the returned-value from the method call is json-stringified as a response body, if true the returned-value is set in the json-request with prop-name "return"
	boolean usrLoginNeeded() default true;
	String prmName() default "";
	boolean prmUrlPart() default false;
	boolean prmUrlRemaining() default false;
	boolean prmLoadByUrl() default false;
	boolean prmBody() default false;
}//HttpMethod

//////////////////////////////////////////////////////////////////////

/** * Created by mbohamad on 19/07/2017.*/
static class TL{
	public static final String TlName=SrvltName+".TL";//Srvlt.packageName
	public TL(HttpServletRequest r,HttpServletResponse n,Writer o){h.req=r;h.rspns=n;out=new Json.Output(o);}

	public H h=new H();
	public Map<String,Object> /**accessing request in json-format*/json;
	public Object bodyData;
	public Date now;
	Map usr;
	/**wrapping JspWriter or any other servlet writer in "out" */
	Json.Output out,/**jo is a single instanceof StringWriter buffer*/jo;

//TL member variables

	/**the static/class variable "tl"*/ static ThreadLocal<TL> tl=new ThreadLocal<TL>();
	public static final String CommentHtml[]={"\n<!--","-->\n"},CommentJson[]={"\n/*","\n*/"};

	public Json.Output jo(){if(jo==null)try{jo=new Json.Output();}catch(Exception x){
		error(x,TlName,".jo:IOEx:");
	}return jo;}

	public Json.Output getOut() throws IOException {return out;}

	/**sets a new TL-instance to the localThread*/
	public static TL Enter(HttpServletRequest r,HttpServletResponse response) throws IOException{
		TL p;if(mth==null || mth.size()==0)staticInit();//Srvlt.
		tl.set(p=new TL(r,response,response.getWriter()));
		p.onEnter();
		return p;}

	private void onEnter()throws IOException {
		h.ip=h.getRequest().getRemoteAddr();
		now=new Date();
		try{Object o=h.req.getContentType();
			o=bodyData=o==null?null
					           :o.toString().contains("json")?Json.Prsr.parse(h.req)
							            :o.toString().contains("part")?h.getMultiParts():null;
			json=o instanceof Map<?, ?>?(Map<String, Object>)o:null;
			if(json==null){
				Map<String,String[]>a=h.req.getParameterMap();
				if(a==null || a.size()<1 ){
					String s=h.req.getQueryString();
					o=s==null?s:Json.Prsr.parse(s);
					if(o instanceof Map)
						json=(Map)o;
				}
			}
			h.logOut=h.var("logOut",h.logOut);
			if(h.getSession().isNew())
				Sql.Tbl.check(this);
			usr=(Map)h.s("usr");
			h.url=h.req.getRequestURI().split("/");
		}catch(Exception ex){
			error(ex,TlName,".onEnter");
		}
		//if(pages==null){rsp.setHeader("Retry-After", "60");rsp.sendError(503,"pages null");throw new Exception("pages null");}
		if(h.logOut)out.w(h.comments[0]).w(TlName).w(".tl.onEnter:\n").o(this).w(h.comments[1]);
	}//onEnter

	private void onExit(){usr=null;h.ip=null;now=null;h.req=null;json=null;out=jo=null;h.rspns=null;}//ssn=null;

	/**unsets the localThread, and unset local variables*/
	public static void Exit(){//throws Exception
		TL p=TL.tl();if(p==null)return;
		Sql.close(p);//changed 2017.7.17
		p.onExit();tl.set(null);}

	public class H{
		public boolean logOut=false;public int urli;
		public String ip,comments[]=CommentJson,url[];
		public HttpServletRequest req;
		public HttpServletResponse rspns;
		public HttpServletRequest getRequest(){return req;}
		public HttpSession getSession(){return req.getSession();}
		public ServletContext getServletContext(){return getSession().getServletContext();}
		Map getMultiParts(){
			Map<Object,Object>m=null;
			if( ServletFileUpload.isMultipartContent(req))try
			{DiskFileItemFactory factory=new DiskFileItemFactory();
				factory.setSizeThreshold(40000000);//MemoryThreshold);
				String path="";//Srvlt.UploadPth;//app(this).getUploadPath();
				String real=context.getRealPath(TL.this, path);//getServletContext().getRealPath(path);
				File f=null,uploadDir;
				uploadDir=new File(real);
				if( ! uploadDir.exists() )
					uploadDir.mkdirs();//mkDir();
				factory.setRepository(uploadDir);
				ServletFileUpload upload=new ServletFileUpload(factory);
				List<FileItem> formItems=upload.parseRequest(req);
				if(formItems!=null && formItems.size()>0 )
				{	m=new HashMap<Object,Object>();
					for(FileItem item:formItems)
					{	String fieldNm=item.getFieldName();
						boolean fld=item.isFormField();//mem=item.isInMemory(),
						if(fld)
						{String v=item.getString();
							Object o=v;
							if(fieldNm.indexOf("json")!=-1)
								o=Json.Prsr.parse(v);
							m.put(fieldNm, o);
						}else{
							long sz=item.getSize();
							if(sz>0){
								String ct=item.getContentType()
										,nm=item.getName();
								int count=0;
								f=new File(uploadDir,nm);
								while(f.exists())
									f=new File(uploadDir,(count++)+'.'+nm);
								m.put(fieldNm,Util.mapCreate(//"name",fieldNm,
										"contentType",ct,"size",sz
										,"fileName",path+f.getName()
								));
								item.write(f);
							}//if sz > 0
						}//if isField else
					}//for(FileItem item:formItems)
				}//if(formItems!=null && formItems.size()>0 )
			}catch(Exception ex){
				error(ex,TlName,".h.getMultiParts");
			}
			//if(ServletFileUpload.isMultipartContent(req))
			return m;
		}//Map getMultiParts()

		/**get a request-scope attribute*/
		public Object r(Object n){return req.getAttribute(String.valueOf(n));}
		/**set a request-scope attribute*/
		public Object r(Object n,Object v){req.setAttribute(String.valueOf(n),v);return v;}
		/**get a session-scope attribute*/
		public Object s(Object n){return getSession().getAttribute(String.valueOf(n));}
		/**set a session-scope attribute*/
		public Object s(Object n,Object v){getSession().setAttribute(String.valueOf(n),v);return v;}
		/**get an application-scope attribute*/
		public Object a(Object n){return getServletContext().getAttribute(String.valueOf(n));}
		/**set an application-scope attribute*/
		public void a(Object n,Object v){getServletContext().setAttribute(String.valueOf(n),v);}
		/**get variable, a variable is considered
		 1: a parameter from the http request
		 2: if the request-parameter is not null then set it in the session with the attribute-name pn
		 3: if the request-parameter is null then get pn attribute from the session
		 4: if both the request-parameter and the session attribute are null then return null
		 @parameters String pn Parameter/attribute Name
		 HttpSession ss the session to get/set the attribute
		 HttpServletRequest rq the http-request to get the parameter from.
		 @return variable value.*/
		public Object var(String pn){
			HttpSession ss=getSession();
			Object r=null;try{Object sVal=ss.getAttribute(pn);String reqv=req(pn);
				if(reqv!=null&&!reqv.equals(sVal)){ss.setAttribute(pn,r=reqv);//logo(TlName,".h.var(",pn,")reqVal:sesssion.set=",r);
				}
				else if(sVal!=null){r=sVal; //logo(TlName,".h.var(",pn,")sessionVal=",r);
				}}catch(Exception ex){
				ex.printStackTrace();
			}return r;}
		public Number var(String pn,Number r)
		{Object x=var(pn);return x==null?r:x instanceof Number?(Number)x:Double.parseDouble(x.toString());}
		public String var(String pn,String r)
		{Object x=var(pn);return x==null?r:String.valueOf(x);}
		public boolean var(String pn,boolean r)
		{Object x=var(pn);return x==null?r:x instanceof Boolean?(Boolean)x:Boolean.parseBoolean(x.toString());}
		/**mostly used for enums , e.g. "enum Screen"*/
		public <T>T var(String n,T defVal) {
			String r=req(n);
			if(r!=null)
				s(n,defVal=Util.parse(r,defVal));
			else{
				Object s=s(n);
				if(s==null)
					s(n,defVal);
				else{Class c=defVal.getClass();
					if(c.isAssignableFrom(s.getClass()))
						defVal=(T)s;//s(n,defVal=(T)s); //changed 2016.07.18
					else
						log(TlName,".h.var(",n,",<T>",defVal,"):defVal not instanceof ssnVal:",s);//added 2016.07.18
				}
			}return defVal;
		}

		public Object reqo(String n){
			if(json!=null )
			{Object o=json.get(n);if(o!=null)return o;}
			String r=req.getParameter(n);
			if(r==null)r=req.getHeader(n);
			if(logOut)log(TlName,".h.reqo(",n,"):",r);
			return r;}

		public String req(String n){
			Object o=reqo(n);
			String r=o instanceof String?(String)o:o!=null?o.toString():null;
			return r;}

		public int req(String n,int defval)
		{Object o=reqo(n);
			if(o instanceof Integer)defval=(Integer)o;
			else if(o instanceof Number)defval=((Number)o).intValue();
			else if(o!=null){
				String s=o instanceof String?(String)o:(o.toString());
				defval=Util.parseInt(s, defval);}
			return defval;}

		public Date req(String n,Date defval){
			Object o=req(n);
			if(o instanceof Date)defval=(Date)o;
			else if(o instanceof Number)defval=new Date(((Number)o).longValue());
			else if(o!=null)defval=Util.parseDate(o instanceof String?(String)o:(o.toString()));
			return defval;}

		public double req(String n,double defval) {
			Object o=reqo(n);
			if(o instanceof Double)defval=(Double)o;
			else if(o instanceof Number)defval=((Number)o).doubleValue();
			else if(o!=null){
				String s=o instanceof String?(String)o:(o.toString());
				if(Util.isNum( s ))
					defval=new Double(s);}
			return defval;}

		public <T>T req(String n,T defVal) {
			Object o=reqo(n);if(o instanceof String)
				defVal=Util.parse((String)o,defVal);
			else if( defVal.getClass( ).isInstance( o )) {//o instanceof T
				T o1 = ( T ) o;
				defVal=o1;
			}else if(o!=null)defVal=Util.parse( o.toString(),defVal );
			return defVal;}

		public Object req(String n,Class c) {
			Object o=reqo(n);
			if(c.isInstance( o ))return o;
			else if (o !=null){
				if(c==Date.class && o instanceof Number)
					o=new Date(((Number)o).longValue());
				else{String s=o instanceof String?(String)o:o.toString();
					o=Util.parse(s,c);}}
			return o;}

	}//class H

	/**get the TL-instance for the current Thread*/
	public static TL tl(){Object o=tl.get();return o instanceof TL?(TL)o:null;}

	////////////////////////////////
	public String logo(Object...a){String s=null;
		if(a!=null&&a.length>0)
			try{Json.Output o=tl().jo().clrSW();
				for(Object i:a)o.o(i);
				s=o.toStrin_();
				h.getServletContext().log(s);//CHANGED 2016.08.17.10.00
				if(h.logOut){out.flush().
						                        w(h.comments[0]//"\n/*"
						                        ).w(s).w(h.comments[1]//"*/\n"
				);}}catch(Exception ex){
				ex.printStackTrace();
			}return s;}

	/**calls the servlet log method*/
	public void log(Object...s){logA(s);}

	public void logA(Object[]s){try{
		jo().clrSW();
		for(Object t:s)jo.w(String.valueOf(t));
		String t=jo.toStrin_();
		h.getServletContext().log(t);
		if(h.logOut)out.flush().w(h.comments[0]).w(t).w(h.comments[1]);
	}catch(Exception ex){
		ex.printStackTrace();
	}}

	public void error(Throwable x,Object...p){try{
		String s=jo().clrSW().w("error:").o(p,x).toString();
		h.getServletContext().log(s);
		if(h.logOut)out.w(h.comments[0]//"\n/*
		).w("error:").w(s.replaceAll("<", "&lt;"))
				            .w("\n---\n").o(x).w(h.comments[1] );
		if(x!=null)x.printStackTrace();}
	catch(Exception ex){
		ex.printStackTrace();
	}}

	public Json.Output o(Object...a)throws IOException{if(out!=null&&out.w!=null)for(Object s:a)out.w.write(s instanceof String?(String)s:String.valueOf(s));return out;}
	/**get a pooled jdbc-connection for the current Thread, calling the function dbc()*/
	Connection dbc()throws SQLException {
		TL p=this;//Object s=context.Sql.reqCon.str,o=p.s(s);
		Object[]a= Sql.stack(p,null);//o instanceof Object[]?(Object[])o:null;
		//o=a==null?null:a[0];
		if(a[0]==null)//o==null||!(o instanceof Connection))
			a[0]= Sql.c();
		return (Connection)a[0];}

}//class TL

//////////////////////////////////////////////////////////////////////

static class Util{ //utility methods

	public static Map<Object, Object> mapCreate(Object...p)
	{Map<Object, Object> m=new HashMap<Object,Object>();//null;
		return p.length>0?maPSet(m,p):m;}

	public static Map<Object, Object> mapSet(Map<Object, Object> m,Object...p){return maPSet(m,p);}

	public static Map<Object, Object> maPSet(Map<Object, Object> m,Object[]p){
		for(int i=0;i<p.length;i+=2)m.put(p[i],p[i+1]);return m;}

	public final static java.text.SimpleDateFormat
			dateFormat=new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

	public static Integer[]parseInts(String s){
		java.util.Scanner b=new java.util.Scanner(s),
				c=b.useDelimiter("[\\s\\.\\-/\\:A-Za-z,]+");
		List<Integer>l=new LinkedList<Integer>();
		while(c.hasNextInt()){
			//if(c.hasNextInt())else c.skip();
			l.add(c.nextInt());
		}c.close();b.close();
		Integer[]a=new Integer[l.size()];l.toArray(a);
		return a;}

	static Date parseDate(String s){
		Integer[]a=parseInts(s);int n=a.length;
		if(n<2){long l=Long.parseLong(s);
			Date d=new Date(l);
			return d;}
		java.util.GregorianCalendar c=new java.util.GregorianCalendar();
		c.set(n>0?a[0]:0,n>1?a[1]-1:0,n>2?a[2]:0,n>3?a[3]:0,n>4?a[4]:0);
		return c.getTime();}

	/**returns a format string of the date as yyyy/MM/dd hh:mm:ss*/
	public static String formatDate(Date p){return p==null?"":dateFormat.format(p);}

	static String format(Object o)throws Exception {
		if(o==null)return null;StringBuilder b=new StringBuilder("\"");
		String a=o.getClass().isArray()?new String((byte[])o):o.toString();
		for(int n=a.length(),i=0;i<n;i++)
		{	char c=a.charAt(i);if(c=='\\')b.append('\\').append('\\');
		else if(c=='"')b.append('\\').append('"');
		else if(c=='\n')b.append('\\').append('n');//.append("\"\n").p(indentation).append("+\"");
		else if(c=='\r')b.append('\\').append('r');
		else if(c=='\t')b.append('\\').append('t');
		else if(c=='\'')b.append('\\').append('\'');
		else b.append(c);
		}return b.append('"').toString();}

	/**return the integer-index of the occurrence of element-e in the array-a, or returns -1 if not found*/
	public static int indexOf(Object[]a,Object e){int i=a.length;while(--i>-1&&(e!=a[i])&&(e==null||!e.equals(a[i])));return i;}

	static boolean eq(Object a,Object e){
		if(a==e||(a!=null&&a.equals(e)))return true;//||(a==null&&e==null)
		return (a==null)?false:a.getClass().isArray()?indexOf((Object[])a,e)!=-1:false;}

	public static List<Object>lst(Object...p){List<Object>r=new LinkedList<Object>();for(Object o:p)r.add(o);return r;}

	public static boolean isNum(String v){
		int i=-1,n=v!=null?v.length():0;
		char c=n>0?v.charAt(0):'\0';
		boolean b=n>0;
		if(n>2&&c=='0')
		{c=v.charAt(1);
			if(c=='X'||c=='x')
			{i=1;
				while(b && (++i)<n){
					c=v.charAt(i);
					b=(c>='0'&&c<='9') || (c>='A'&&c<='F')  || (c>='a'&&c<='f') ;
				}
				return b;}}
		while(b&& c!='.'&& i+1<n)
		{c=++i<n?v.charAt(i):'\0';
			b= Character.isDigit(c)||c=='.';
		}
		if(c=='.') while(b&& i+1<n)
		{c=++i<n?v.charAt(i):'\0';
			b= Character.isDigit(c);
		}
		return b;
	}

	public static int parseInt(String v,int dv){
		if(isNum(v) )try{dv=Integer.parseInt(v);}
		catch(Exception ex){//changed 2016.06.27 18:28
			TL.tl().error(ex, SrvltName,".Util.parseInt:",v,dv);
		}return dv;}

	public static <T>T parse(String s,T defval){
		if(s!=null)try{
			Class<T> ct=(Class<T>) defval.getClass();
			Class c=ct;
			boolean b=c==null?false:c.isEnum();
			if(!b){c=ct.getEnclosingClass();b=c==null?false:c.isEnum();}
			if(b){
				for(Object o:c.getEnumConstants())
					if(s.equalsIgnoreCase(o.toString()))
						return (T)o;
			}}catch(Exception x){//changed 2016.06.27 18:28
			TL.tl().error(x, SrvltName,".Util.<T>T parse(String s,T defval):",s,defval);
		}
		return defval;}

	public static Object parse(String s,Class c){
		if(s!=null)try
		{	if(String.class.equals(c))return s;
		else if(Number.class.isAssignableFrom(c)||c.isPrimitive()) {
			if (Integer.class.equals(c)|| "int"   .equals(c.getName())) return new Integer(s);
			else if (Double .class.equals(c)|| "double".equals(c.getName())) return new Double(s);
			else if (Float  .class.equals(c)|| "float" .equals(c.getName())) return new Float(s);
			else if (Short  .class.equals(c)|| "short" .equals(c.getName())) return new Short(s);
			else if (Long   .class.equals(c)|| "long"  .equals(c.getName())) return new Long(s);
			else if (Byte   .class.equals(c)|| "byte"  .equals(c.getName())) return new Byte(s);
		}///else return new Integer(s);}
		else if(Boolean.class.equals(c)||(c.isPrimitive()&&"boolean".equals(c.getName())))return new Boolean(s);
		else if(Date.class.equals(c))return parseDate(s);
		else if(Character.class.isAssignableFrom(c)||(c.isPrimitive()&&"char".equals(c.getName())))
			return s.length()<1?'\0':s.charAt(0);
		else if(URL.class.isAssignableFrom(c))try
		{return new URL("file:" +TL.tl().h.getServletContext().getContextPath()+'/'+s);}
		catch (Exception ex) {
			TL.tl().error(ex,SrvltName,".Util.parse:URL:p=",s," ,c=",c);
		}
			boolean b=c==null?false:c.isEnum();
			if(!b){Class ct=c.getEnclosingClass();b=ct==null?false:ct.isEnum();if(b)c=ct;}
			if(b){
				for(Object o:c.getEnumConstants())
					if(s.equalsIgnoreCase(o.toString()))
						return o;
			}
			return Json.Prsr.parse(s);
		}catch(Exception x){//changed 2016.06.27 18:28
			TL.tl().error(x, SrvltName,".Util.<T>T parse(String s,Class):",s,c);
		}
		return s;}

	public static String md5(String s){
		if(s!=null)try{java.security.MessageDigest m=
				               java.security.MessageDigest.getInstance("MD5");//m.update(s.getBytes());
			String r=toHexString( m.digest(s.getBytes()) );//changed 2018.12.25.08.51//java.util.Base64.getEncoder().encodeToString(m.digest(s.getBytes()));
			return r;
		}catch(Exception x){//changed 2016.06.27 18:28
			TL.tl().error(x, SrvltName,".Util.md5(String s):",s);
		}
		return "";}

	public static String b64d(String s){
		if(s!=null)try{
			byte[]m=java.util.Base64.getDecoder().decode( s );
			String r= new String(m,"UTF-8");
			return r;
		}catch(Exception x){//changed 2016.06.27 18:28
			TL.tl().error(x, SrvltName,".Util.b64d(String s):",s);
		}
		return "";}

	public static String b64e(String s){
		if(s!=null)try{
			byte[]m=s.getBytes();
			String r=java.util.Base64.getEncoder().encodeToString( m );
			return r;
		}catch(Exception x){//changed 2016.06.27 18:28
			TL.tl().error(x, SrvltName,".Util.b64e(String s):",s);
		}
		return "";}

	static String readString(BufferedReader r) throws IOException {
		StringBuilder b = new StringBuilder();
		int c = 0;
		String line = r.readLine();
		while(line != null) {
			if(c++ > 0) b.append('\n');
			b.append(line);
			line = r.readLine();
		}
		return b.toString();
	}//readString

	static String toHexString(byte[]a){
		StringBuilder b=new StringBuilder(  );
		for(byte v:a){
			int i=Byte.toUnsignedInt( v );
			b.append( Integer.toHexString( i/16 ))
					.append( Integer.toHexString( i%16 ));}
		return b.toString();
	}//created 2018.12.25.08.51

}//class Util


//////////////////////////////////////////////////////////////////////

static class Sql {
	/**returns a jdbc pooled Connection.
	 uses MysqlConnectionPoolDataSource with a database from the enum context.Sql.url.str,
	 sets the pool as an application-scope attribute named context.Sql.pool.str
	 when first time called, all next calls uses this context.Sql.pool.str*/
	public static synchronized Connection c()throws SQLException {
		TL t=TL.tl();
		Object[]p=null,a=stack(t,null);//Object[])t.s(context.Sql.reqCon.str);
		Connection r=(Connection)a[0];//a ==null?null:
		if(r!=null)return r;
		MysqlConnectionPoolDataSource d=(MysqlConnectionPoolDataSource)t.h.a(context.DB.pool.str);
		r=d==null?null:d.getPooledConnection().getConnection();
		if(r!=null)
			a[0]=r;//changed 2017.07.14
		else try
		{try{int x=context.getContextIndex(t);
			t.log(SrvltName,".Sql.c:1:getContextIndex:",x);
			if(x!=-1)
			{	p=c(t,x,x,x,x);t.log(SrvltName,".Sql.c:1:c2:",p);
				r=(Connection)p[1];
				return r;}
		}catch(Exception e){
			t.log(SrvltName,".Sql.MysqlConnectionPoolDataSource:1:",e);
		}
			String[]dba=context.DB.dbName.a
					,sra=context.DB.server.a
					,una=context.DB.un.a
					,pwa=context.DB.pw.a;//CHANGED: 2016.02.18.10.32
			for(int idb=0;r==null&&idb<dba.length;idb++)
				for(int iun=0;r==null&&iun<una.length;iun++)
					for(int ipw=0;r==null&&ipw<pwa.length;ipw++)//n=context.Sql.len()
						for(int isr=0;r==null&&isr<sra.length;isr++)try
						{	p=c(t,idb,iun,ipw,isr);
							r=(Connection)p[1];
							if(t.h.logOut)t.log("new "+context.DB.pool.str+":"+p[0]);
						}catch(Exception e){
							t.log(SrvltName,".Sql.MysqlConnectionPoolDataSource:",idb,",",isr,",",iun,ipw,t.h.logOut?p[2]:"",",",e);
						}
		}catch(Throwable e){
			t.error(e,SrvltName,".Sql.MysqlConnectionPoolDataSource:throwable:");
		}//ClassNotFoundException
		if(t.h.logOut)t.log(context.DB.pool.str+":"+(p==null?null:p[0]));
		if(r==null)try
		{r=java.sql.DriverManager.getConnection
				                          ("jdbc:mysql://"+context.DB.server.str
						                           +"/"+context.DB.dbName.str
						                          ,context.DB.un.str,context.DB.pw.str
				                          );Object[]b={r,null};
			t.h.s(context.DB.reqCon.str,b);
		}catch(Throwable e){
			t.error(e,SrvltName,".Sql.DriverManager:");
		}
		return r;}
	public static synchronized Object[]c(TL t,int idb,int iun,int ipw,int isr) throws SQLException{
		MysqlConnectionPoolDataSource d=new MysqlConnectionPoolDataSource();
		String ss=null,s=context.DB.dbName.a[Math.min(context.DB.dbName.a.length-1,idb)];
		if(t.h.logOut)ss="\ndb:"+s;
		d.setDatabaseName(s);d.setPort(3306);
		s=context.DB.server.a[Math.min(context.DB.server.a.length-1,isr)];
		if(t.h.logOut)ss+="\nsrvr:"+s;
		d.setServerName(s);
		s=context.DB.un.a[Math.min(context.DB.un.a.length-1,iun)];if(t.h.logOut)ss+="user:"+s;
		d.setUser(s);
		s=context.DB.pw.a[Math.min(context.DB.pw.a.length-1,ipw)];if(t.h.logOut)ss+="\npw:"+s;
		d.setPassword(s);
		Connection r=d.getPooledConnection().getConnection();
		t.h.a(context.DB.pool.str,d);
		Object[]a={d,r,ss};//,b={r,null};t.s(context.Sql.reqCon.str,b);
		stack(t,r);
		return a;}
	/**returns a jdbc-PreparedStatement, setting the variable-length-arguments parameters-p, calls dbP()*/
	public static PreparedStatement p( String sql, Object...p)throws SQLException{return P(sql,p);}
	/**returns a jdbc-PreparedStatement, setting the values array-parameters-p, calls TL.dbc() and log()*/
	public static PreparedStatement P(String sql,Object[]p)throws SQLException{return P(sql,p,true);}
	public static PreparedStatement P(String sql,Object[]p,boolean odd)throws SQLException {
		TL t=TL.tl();Connection c=t.dbc();
		PreparedStatement r=c.prepareStatement(sql);if(t.h.logOut)
			t.log(SrvltName,"("+t+").Sql.P(sql="+sql+",p="+p+",odd="+odd+")");
		if(odd){if(p.length==1)
			r.setObject(1,p[0]);else
			for(int i=1,n=p.length;p!=null&&i<n;i+=2)if((!(p[i] instanceof List))
					                                            && !(p[i] instanceof Tbl.CI) //added 2018.12.17 10:58 for Co.max
					                                         ) // ||!(p[i-1] instanceof List)||((List)p[i-1]).size()!=2||((List)p[i-1]).get(1)!=Tbl.Co.in )
				r.setObject(i/2+1,p[i]);//if(t.logOut)TL.log("dbP:"+i+":"+p[i]);
		}else
			for(int i=0;p!=null&&i<p.length;i++)
			{r.setObject(i+1,p[i]);if(t.h.logOut)t.log("dbP:"+i+":"+p[i]);}
		if(t.h.logOut)t.log("dbP:sql="+sql+":n="+(p==null?-1:p.length)+":"+r);return r;}

	/**returns a jdbc-ResultSet, setting the variable-length-arguments parameters-p, calls dbP()*/
	public static ResultSet r( String sql, Object...p)throws SQLException{return R(sql,p);}//changed 2017.7.17
	/**returns a jdbc-ResultSet, setting the values array-parameters-p, calls dbP()*/
	public static ResultSet R(String sql,Object[]p)throws SQLException{
		PreparedStatement x=P(sql,p,true);
		ResultSet r=x.executeQuery();
		push(r,TL.tl());
		return r;}
	static Object[]stack(TL tl,Connection c){return stack(tl,c,true);}
	static Object[]stack(TL tl,Connection c,boolean createIfNotExists){
		return stack(tl,c,createIfNotExists,false);}
	static Object[]stack(TL tl,Connection c,boolean createIfNotExists,boolean deleteArray){
		if(tl==null)tl=TL.tl();Object o=context.DB.reqCon.str;
		Object[]a=(Object[])tl.h.s(o);
		if(deleteArray)
			tl.h.s(o,a=null);
		else if(a==null&&createIfNotExists)
		{Object[]b={c,null};
			tl.h.s(o,a=b);}
		return a;}
	static List<ResultSet>stack(TL tl){return stack(tl,true);}
	static List<ResultSet>stack(TL tl,boolean createIfNotExists){
		Object[]a=stack(tl,null,createIfNotExists);
		List<ResultSet>l=a==null||a.length<2?null:(List<ResultSet>)a[1];
		if(l==null&&createIfNotExists)
			a[1]=l=new LinkedList<ResultSet>();
		return l;}
	static void push(ResultSet r,TL tl){try{//2017.07.14
		List<ResultSet>l=stack(tl);//if(l==null){stack(tl,null)[1]=l=new LinkedList<ResultSet>();l.add(r);}else
		if(!l.contains(r))
			l.add(r);
	}catch (Exception ex){
		tl.error(ex,SrvltName,".Sql.push");
	}}

	//public static void close(Connection c){close(c,tl());}
	public static void close(Connection c,TL tl){
		try{if(c!=null){
			List<ResultSet>a=stack(tl,false);
			if(a==null||a.size()<1)
				tl.h.s(context.DB.reqCon.str,a=null);
			if(a==null)
				c.close();}
		}catch(Exception e){
			e.printStackTrace();
		}}
	public static void close(TL tl){
		try{Object[]a=stack(tl,null,false);
			Connection c=a==null?null:(Connection) a[0];
			if(c!=null)close(c,tl);
		}catch(Exception e){
			e.printStackTrace();
		}}
	public static void close(ResultSet r){close(r,TL.tl(),false);}
	//public static void close(ResultSet r,boolean closeC){close(r,TL.tl(),closeC);}
	public static void close(ResultSet r,TL tl){close(r,tl,false);}
	public static void close(ResultSet r,TL tl,boolean closeC){
		if(r!=null)try{
			Statement s=r.getStatement();
			Connection c=closeC?s.getConnection():null;
			List<ResultSet>l=stack(tl,false);
			if(l!=null){l.remove(r);
				if( l.size()<1 )
					l=null;}
			r.close();s.close();
			if(l==null&&closeC)close(c,tl);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**returns a string or null, which is the result of executing sql,
	 calls dpR() to set the variable-length-arguments parameters-p*/
	public static String q1str(String sql,Object...p)throws SQLException{return q1Str(sql,p);}
	public static String q1Str(String sql,Object[]p)throws SQLException
	{String r=null;ResultSet s=null;try{s=R(sql,p);r=s.next()?s.getString(1):null;}finally{close(s);}return r;}//CHANGED:2015.10.23.16.06:closeRS ; CHANGED:2011.01.24.04.07 ADDED close(s,dbc());
	public static String newUuid()throws SQLException{return q1str("select uuid();");}
	/**returns an java obj, which the result of executing sql,
	 calls dpR() to set the variable-length-arguments parameters-p*/
	public static Object q1obj(String sql,Object...p)throws SQLException{return q1Obj(sql,p);}
	public static Object q1Obj(String sql,Object[]p)throws SQLException {
		ResultSet s=null;try{s=R(sql,p);return s.next()?s.getObject(1):null;}finally{close(s);}}
	public static <T>T q1(String sql,Class<T>t,Object[]p)throws SQLException {
		ResultSet s=null;try{s=R(sql,p);return s.next()?s.getObject(1,t):null;}finally{close(s);}}
	/**returns an integer or df, which the result of executing sql,
	 calls dpR() to set the variable-length-arguments parameters-p*/
	public static int q1int(String sql,int df,Object...p)throws SQLException{return q1Int(sql,df,p);}
	public static int q1Int(String sql,int df,Object[]p)throws SQLException
	{ResultSet s=null;try{s=R(sql,p);return s.next()?s.getInt(1):df;}finally{close(s);}}//CHANGED:2015.10.23.16.06:closeRS ;
	/**returns a double or df, which is the result of executing sql,
	 calls dpR() to set the variable-length-arguments parameters-p*/
	public static double q1dbl(String sql,double df,Object...p)throws SQLException
	{ResultSet s=null;try{s=R(sql,p);return s.next()?s.getDouble(1):df;}finally{close(s);}}//CHANGED:2015.10.23.16.06:closeRS ;
	/**returns as an array of rows of arrays of columns of values of the results of the sql
	 , calls dbL() setting the variable-length-arguments values parameters-p*/
	public static Object[][]q(String sql,Object...p)throws SQLException{return Q(sql,p);}
	public static Object[][]Q(String sql,Object[]p)throws SQLException
	{List<Object[]>r=L(sql,p);Object b[][]=new Object[r.size()][];r.toArray(b);r.clear();return b;}
	/**return s.getMetaData().getColumnCount();*/
	public static int cc(ResultSet s)throws SQLException{return s.getMetaData().getColumnCount();}
	/**calls L()*/
	public static List<Object[]> l(String sql,Object...p)throws SQLException{return L(sql,p);}
	/**returns a new linkedList of the rows of the results of the sql
	 ,each row/element is an Object[] of the columns
	 ,calls dbR() and dbcc() and dbclose(ResultSet,TL.dbc())*/
	public static List<Object[]> L(String sql,Object[]p)throws SQLException {
		TL t=TL.tl();ResultSet s=null;List<Object[]> r=null;try{s=R(sql,p);Object[]a;r=new LinkedList<Object[]>();
			int cc=cc(s);while(s.next()){r.add(a=new Object[cc]);
				for(int i=0;i<cc;i++){a[i]=s.getObject(i+1);
				}}return r;}finally{close(s,t);//CHANGED:2015.10.23.16.06:closeRS ;
			if(t.h.logOut)try{t.log(t.jo().w(SrvltName).w(".Sql.L:sql=").o(sql).w(",prms=").o(p).w(",return=").o(r).toStrin_());}
			catch(IOException x){
				t.error(x,SrvltName,".Sql.List:",sql);
			}}}

	public static List<Integer[]>qLInt(String sql,Object...p)throws SQLException{return qLInt(sql,p);}//2017.07.14
	public static List<Integer[]>QLInt(String sql,Object[]p)throws SQLException{//2017.07.14
		TL tl=TL.tl();
		ResultSet s=null;
		List< Integer[]> r=null;
		try{s=R(sql,p);
			Integer[]a;
			r=new LinkedList<Integer[]>();
			int cc=cc(s);
			while(s.next()){
				r.add(a=new Integer[cc]);
				for(int i=0;i<cc;i++)
					a[i]=s.getInt(i+1);
			}return r;
		}finally
		{close(s,tl);
			if(tl.h.logOut)try{tl.log(tl.jo().w(SrvltName).w(".Sql.Lt:sql=")
					                          .o(sql).w(",prms=").o(p).w(",return=").o(r).toStrin_());}
			catch(IOException x){
				tl.error(x,SrvltName,".Sql.Lt:",sql);
			}
		}
	}

	public static List<Object> q1colList(String sql,Object...p)throws SQLException {
		ResultSet s=null;List<Object> r=null;try{s=R(sql,p);r=new LinkedList<Object>();
			while(s.next())r.add(s.getObject(1));return r;}
		finally{TL t=TL.tl();close(s,t);if(t.h.logOut)
			try{t.log(t.jo().w(SrvltName).w(".Sql.q1colList:sql=")//CHANGED:2015.10.23.16.06:closeRS ;
					          .o(sql).w(",prms=").o(p).w(",return=").o(r).toStrin_());}catch(IOException x){
				t.error(x,SrvltName,".Sql.q1colList:",sql);
			}}}

	public static <T>List<T> q1colTList(String sql,Class<T>t,Object...p)throws SQLException {
		ResultSet s=null;List<T> r=null;try{s=R(sql,p);r=new LinkedList<T>();//Class<T>t=null;
			while(s.next())r.add(
					s.getObject(1,t));return r;}
		finally{TL tl=TL.tl();close(s,tl);if(tl.h.logOut)
			try{tl.log(tl.jo().w(SrvltName).w(".Sql.q1colList:sql=")//CHANGED:2015.10.23.16.06:closeRS ;
					           .o(sql).w(",prms=").o(p).w(",return=").o(r).toStrin_());}catch(IOException x){
				tl.error(x,SrvltName,".Sql.q1colList:",sql);
			}}}

	public static Object[] q1col(String sql,Object...p)throws SQLException
	{List<Object> l=q1colList(sql,p);Object r[]=new Object[l.size()];l.toArray(r);l.clear();return r;}
	public static <T>T[] q1colT(String sql,Class<T>t,Object...p)throws SQLException
	{List<T> l=q1colTList(sql,t,p);T[]r=(T[]) java.lang.reflect.Array.newInstance(t,l.size());l.toArray(r);l.clear();return r;}
	/**returns a row of columns of the result of sql
	 ,calls dbR(),dbcc(),and dbclose(ResultSet,TL.dbc())*/
	public static Object[] q1row(String sql,Object...p)throws SQLException{return q1Row(sql,p);}
	public static Object[] q1Row(String sql,Object[]p)throws SQLException {
		ResultSet s=null;try{s=R(sql,p);Object[]a=null;int cc=cc(s);if(s.next())
		{a=new Object[cc];for(int i=0;i<cc;i++)try{a[i]=s.getObject(i+1);}
		catch(Exception ex){
			TL.tl().error(ex,SrvltName,".Sql.q1Row:",sql);a[i]=s.getString(i+1);
		}}
			return a;}finally{close(s);}}//CHANGED:2015.10.23.16.06:closeRS ;
	/**returns the result of (e.g. insert/update/delete) sql-statement
	 ,calls dbP() setting the variable-length-arguments values parameters-p
	 ,closes the preparedStatement*/
	public static int x(String sql,Object...p)throws SQLException{return X(sql,p);}
	public static int X(String sql,Object[]p)throws SQLException {
		int r=-1;try{PreparedStatement s=P(sql,p,false);r=s.executeUpdate();s.close();return r;}
		finally{TL t=TL.tl();if(t.h.logOut)try{
			t.log(t.jo().w(SrvltName).w(".Sql.x:sql=").o(sql).w(",prms=").o(p).w(",return=").o(r).toStrin_());}
		catch(IOException x){
			t.error(x,SrvltName,".Sql.X:",sql);
		}}}
	/**output to tl.out the Json.Output.oRS() of the query*/
	public static void q2json(String sql,Object...p)throws SQLException{
		ResultSet s=null;
		TL tl=TL.tl();
		try{
			s=R(sql,p);
			try{
				tl.getOut() .o(s); // (new Json.Output()) // TODO:investigate where the Json.Output.w goes
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		finally
		{close(s,tl);
			if(tl.h.logOut)try{
				tl.log(tl.jo().w(SrvltName).w(".Sql.L:q2json=")
						       .o(sql).w(",prms=").o(p).toStrin_());
			}catch(IOException x){
				tl.error(x,SrvltName,".Sql.q1json:",sql);
			}
		}
	}
	/**return a list of maps , each map has as a key a string the name of the column, and value obj*/
	static List<Map<String,Object>>json(String sql,Object...p) throws SQLException{return Lst(sql,p);}
	static List<Map<String,Object>>Lst(String sql,Object[ ]p) throws SQLException{
		List<Map<String,Object>>l=new LinkedList< Map < String ,Object>>();ItTbl i=new ItTbl(sql,p);
		List<String>cols=new LinkedList<String>();
		for(int j=1;j<=i.row.cc;j++)cols.add(i.row.m.getColumnLabel(j));
		for(ItTbl.ItRow w:i){Map<String,Object>m= new HashMap<String,Object>();l.add(m);
			for(Object o:w)m.put(cols.get(w.col-1),o);
		}return l;}
	public static class ItTbl implements Iterator<ItTbl.ItRow>,Iterable<ItTbl.ItRow>{
		public ItRow row=new ItRow();
		public ItRow getRow(){return row;}
		public static ItTbl it(String sql,Object...p){return new ItTbl(sql,p);}
		public ItTbl(String sql,Object[]p){
			try {init(Sql.R(sql, p));}
			catch (Exception e) {
				TL.tl().logo(SrvltName,".Sql.ItTbl.<init>:Exception:sql=",sql,",p=",p," :",e);
			}}
		public ItTbl(ResultSet o) throws SQLException{init(o);}
		public ItTbl init(ResultSet o) throws SQLException {
			row.rs=o;row.m=o.getMetaData();row.row=row.col=0;
			row.cc=row.m.getColumnCount();return this;}
		static final String ErrorsList=SrvltName+".Sql.ItTbl.errors";
		@Override public boolean hasNext(){
			boolean b=false;try {if(b=row!=null&&row.rs!=null&&row.rs.next())row.row++;
			else Sql.close(row.rs);//CHANGED:2015.10.23.16.06:closeRS ; 2017.7.17
			}catch (SQLException e) {//e.printStackTrace();
				TL t=TL.tl();//changed 2016.06.27 18:05
				final String str=SrvltName+".Sql.ItTbl.next";
				t.error(e,str);
				List l=(List)t.json.get(ErrorsList);//t.response
				if(l==null)t.json.put(ErrorsList,l=new LinkedList());//t.response
				l.add(Util.lst(str,row!=null?row.row:-1,e));
			}return b;}
		@Override public ItRow next() {if(row!=null)row.col=0;return row;}
		@Override public void remove(){throw new UnsupportedOperationException();}
		@Override public Iterator<ItRow>iterator(){return this;}
		public class ItRow implements Iterator<Object>,Iterable<Object>{
			ResultSet rs;int cc,col,row;ResultSetMetaData m;
			public int getCc(){return cc;}
			public int getCol(){return col;}
			public int getRow(){return row;}
			@Override public Iterator<Object>iterator(){return this;}
			@Override public boolean hasNext(){return col<cc;}
			@Override public Object next(){
				try {return rs==null?null:rs.getObject(++col);}
				catch (SQLException e) {//changed 2016.06.27 18:05
					TL t=TL.tl();
					final String str=SrvltName+".Sql.ItTbl.ItRow.next";
					t.error(e,str);
					List l=(List)t.json.get(ErrorsList);//t.response
					if(l==null)t.json.put(ErrorsList,l=new LinkedList());//t.response
					l.add(Util.lst(str,row,col,e));
				}//.printStackTrace();}
				return null;}
			@Override public void remove(){throw new UnsupportedOperationException();}
			public int nextInt(){
				try {return rs==null?-1:rs.getInt(++col);}
				catch (SQLException e) {
					e.printStackTrace();
				}
				return -1;}
			public String nextStr(){
				try {return rs==null?null:rs.getString(++col);}
				catch (SQLException e) {
					e.printStackTrace();
				}
				return null;}
		}//ItRow
	}//ItTbl
	/**represents one entity , one row from a table in a relational database*/
	public abstract static class Tbl implements Json.Output.I {//<PK>
		// /**encapsulating Html-form fields, use annotation Form.F for defining/mapping member-variables to html-form-fields*/ public abstract static class Form{
		@Override public String toString(){return toJson();}

		/**get table name*/public abstract String getName();

		public Json.Output jsonOutput(Json.Output o,String ind,String path)throws java.io.IOException{return jsonOutput( o,ind,path,true );}
		public Json.Output jsonOutput(Json.Output o,String ind,String path,boolean closeBrace)throws java.io.IOException {
			//if(o.comment)o.w("{//TL.Form:").w('\n').p(ind);else//.w(p.getClass().toString())
			o.w('{');
			CI[]a=columns();
			String i2=ind+'\t';
			o.w("\"class\":").oStr(getClass().getSimpleName(),ind);//w("\"name\":").oStr(p.getName(),ind);
			for(CI f:a)try
			{	o.w(',').oStr(f.getName(),i2).w(':')
						 .o(v(f),ind,o.comment?path+'.'+f.getName():path);
				if(o.comment)o.w("//").w(f.toString()).w("\n").p(i2);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if(closeBrace){
				if(o.comment)
					o.w("}//Sql.Tbl&cachePath=\"").p(path).w("\"\n").p(ind);
				else o.w('}');
			}
			return o; }

		public Json.Output jsonOutput(Json.Output o,String ind,String i2,String path,boolean closeBrace,CI f)throws java.io.IOException {
			o.w(',').oStr(f.getName(),i2).w(':')
					.o(v(f),ind,o.comment?path+'.'+f.getName():path);
			if(o.comment)o.w("//").w(f.toString()).w("\n").p(i2);
			return o; }

		public String toJson(){Json.Output o= TL.tl().jo().clrSW();try {jsonOutput(o, "", "");}catch (IOException ex) {}return o.toString();}

		public Tbl readReq(String prefix){
			TL t=TL.tl();CI[]a=columns();
			for(CI f:a){
				String s=t.h.req(prefix==null||prefix.length()<1?prefix+f:f.toString());
				Class <?>c=s==null?null:f.getType();
				Object v=null;try {
					if(s!=null)v=Util.parse(s,c);
					v(f,v);//f.set(this, v);
				}catch (Exception ex) {// IllegalArgumentException,IllegalAccessException
					t.error(ex,SrvltName,".Sql.Tbl.readReq:t=",this," ,field="
							,f+" ,c=",c," ,s=",s," ,v=",v);
				}
			}
			return this;}
//{

		public abstract CI[]columns();//public abstract FI[]flds();

		public Object[]valsForSql(){
			CI[]a=columns();
			Object[]r=new Object[a.length];
			int i=-1;
			for(CI f:a){i++;
				r[i]=valForSql(a[i]);
			}return r;/*
		public Object[]_vals(){
			CI[]a=columns();//Field[]a=fields();
			Object[]r=new Object[a.length];
			int i=-1;
			for(CI f:a){i++;
				r[i]=v(a[i]);
			}return r;}*/}

		public Object valForSql(CI f){
			Object o=v(f);
			if(o instanceof Map)
				o=Json.Output.out( o );
			return o;}

		public Tbl vals (Object[]p){
			int i=-1;CI[]a=columns();//Field[]a=fields();
			for(CI f:a)
				v(f,p[++i]);
			return this;}

		public Map asMap(){ return asMap(null);}

		public Map asMap(Map r){
			CI[]a=columns();//Field[]a=fields();
			if(r==null)r=new HashMap();
			int i=-1;
			for(CI f:a){i++;
				r.put(f.getName(),v(a[i]));
			}return r;}

		public Tbl fromMap (Map p){
			CI[]a=columns();//Field[]a=fields();
			for(CI f:a){String n=f.getName();
				if(p.containsKey(n))
					v(f,p.get(n));}
			return this;}

		public Tbl v(CI p,Object v){return v(p.f(),v);}//this is beautiful(tear running down cheek)

		public Object v(CI p){return v(p.f());}//this is beautiful(tear running down cheek)

		Tbl v(Field p,Object v){//this is beautiful(tear running down cheek)
			try{Class <?>t=p.getType();
				if(v!=null && !t.isAssignableFrom( v.getClass() ))//t.isEnum()||t.isAssignableFrom(URL.class))
					v=Util.parse(v instanceof String?(String)v:String.valueOf(v),t);
				p.set(this,v);
			}catch (Exception ex) {
				TL.tl().error(ex,SrvltName,".Sql.Tbl.v(",this,",",p,",",v,")");
			}
			return this;}

		Object v(Field p){//this is beautiful(tear running down cheek)
			try{return p.get(this);}
			catch (Exception ex) {//IllegalArgumentException,IllegalAccessException
				TL.tl().error(ex,SrvltName,".Sql.Tbl.v(",this,",",p,")");return null;
			}}

		/**Field annotation to designate a java member for use in a dbTbl-column/field*/
		@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
		public @interface F{}

		/**Interface for enum-items from different forms and sql-tables ,
		 * the enum items represent a reference Column Fields for identifing the column and selection.*/
		public interface CI{public Field f();public String getName();public Class getType();}//interface I

//}//public abstract static class Form

		/**Sql-Column Interface, for enum -items that represent columns in sql-tables
		 * the purpose of creating this interface is to centerlize
		 * the definition of the names of columns in java source code*/

		public abstract Object[]wherePK();//{Object[]c=pkcols(),v=pkvals(),a=new Object[c.length+v.length];for(int i=0;i<c.length;i++){a[i*2]=c[i];a[i*2+1]=v[i];}return a;}

		public static CI[]cols(CI...p){return p;}
		public static Object[]where(Object...p){return p;}
		//public abstract CI pkc(int i);public abstract CI[]pkcols();public abstract int pkcn();
		//public abstract PK pkv(int i);public abstract PK[]pkvals();
		//public abstract PK[]pkv(PK[]v);
		//public PK[]pka(PK...p){return p;}//static

		public String sql(CI[]cols,Object[]where){
			return sql(cols,where,null,null,getName());}

		public static String sql(CI[]cols,Object[]where,String name){
			return sql( cols, where,null,null,name);}//StringBuilder sql,

		public String sql(CI[]cols,Object[]where,CI[]groupBy){
			return sql(cols,where,groupBy,null,getName());}

		public String sql(String cols,Object[]where,CI[]groupBy,CI[]orderBy) {
			StringBuilder sql=new StringBuilder("select ");
			sql.append(cols);//Co.generate(sql,cols);
			sql.append(" from `").append(getName()).append("` ");
			if(where!=null&&where.length>0)
				Co.where(sql, where);
			if(groupBy!=null && groupBy.length>0){
				sql.append(" group by ");
				Co.generate(sql,groupBy);}
			if(orderBy!=null && orderBy.length>0){
				sql.append(" order by ");
				Co.generate(sql,orderBy);}
			return sql.toString();}

		public static String sql(CI[]cols,Object[]where,CI[]groupBy,CI[]orderBy,String dbtName){
			return sql(cols,where,groupBy,orderBy,dbtName,null);}

		public static String sql(CI[]cols,Object[]where,CI[]groupBy,CI[]orderBy,String dbtName,String dbn){
			//if(cols==null)cols=columns();
			StringBuilder sql=new StringBuilder("select ");
			Co.generate( sql,cols );//sql.append(cols);
			if(dbn==null)
				sql.append(" from `").append(dbtName).append("` ");
			else sql.append(" from `").append(dbn).append("`.`").append(dbtName).append("` ");
			if(where!=null&&where.length>0)
				Co.where(sql, where);
			if(groupBy!=null && groupBy.length>0){
				sql.append(" group by ");
				Co.generate(sql,groupBy);}
			if(orderBy!=null && orderBy.length>0){
				sql.append(" order by ");
				Co.generate(sql,orderBy);}
			return sql.toString();}
		/** returns a list of 3 lists,(only the first is mandatory ,the rest are optional)
		 * the 1st is a list for the db-table columns-CI
		 * the 2nd is a list for the db-table-key-indices
		 * the 3rd is a list for row insertion
		 * 4th element is a Class<Sql.Tbl> , a dependency that will be created before this table.
		 *
		 * the 1st list, the definition of the column is a string
		 * , e.i. varchar(255) not null
		 * or e.i. int(18) primary key auto_increment not null
		 * the 2nd list of the db-table key-indices(optional)
		 * each dbt-key-index can be a CI or a list , if a list
		 * each item has to be a List
		 * ,can start with a prefix, e.i. unique , or key`ix1`
		 * , the items of this list should be a CI
		 * ,	or the item can be a list that has as the 1st item the CI
		 * and the 2nd item the length of the index
		 * the third list is optional, for each item in this list
		 * is a list of values to be inserted into the created table
		 */
		public abstract List DBTblCreation(TL tl);
		public void checkDBTCreation(TL tl){
			String dtn=getName();Object o=tl.h.a(SrvltName+":db:show tables");
			if(o==null)
				try {o= Sql.q1colList("show tables");
					tl.h.a(SrvltName+":db:show tables",o);
				} catch (SQLException ex) {
					tl.error(ex, SrvltName+".Sql.Tbl.checkTableCreation:check-pt1:",dtn);
				}
			List l=(List)o;
			try{if(o==null||(!l.contains( dtn )&&!l.contains( dtn.toLowerCase()))){
				List a=DBTblCreation(tl),b=(List)a.get(0);
				if(a.size()>3){
					Class<Tbl> c=(Class)a.get( 3 );
					try{Tbl t=c.newInstance();
						t.checkDBTCreation( tl );
					}catch(Exception ex){
						ex.printStackTrace();
					}}
				StringBuilder sql= new StringBuilder("CREATE TABLE `").append(dtn).append("` (\n");
				CI[]ci=columns();int an,x=0;
				for(CI i:ci){
					if(x>0 )
						sql.append("\n,");
					sql.append('`').append(i).append('`')
							.append(String.valueOf(b.get(x)) );
					x++;}
				an=a.size();b=an>1?(List)a.get(1):b;
				if(an>1)for(Object bo:b)
				{sql.append("\n,");x=0;
					if(bo instanceof CI)
						sql.append("KEY(`").append(bo).append("`)");
					else if(bo instanceof List)
					{	List bl=(List)bo;x=0;boolean keyHeadFromList=false;
						for(Object c:bl){
							boolean s=c instanceof String;
							if(x<1 && !s&& !keyHeadFromList)
								sql.append("KEY(");
							if(x>0)
								sql.append(',');//in the list
							if(s){sql.append((String)c);if(x==0){x--;keyHeadFromList=true;}}
							else {l=c instanceof List?(List)c:null;
								sql.append('`').append(
										l==null?String.valueOf(c)
												:String.valueOf(l.get(0))
								).append("`");
								if(l!=null&&l.size()>1)
									sql.append('(').append(l.get(1)).append(')');
							}x++;
						}sql.append(")");
					}else
						sql.append(bo);
				}
				sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 ;");
				tl.log(SrvltName,".Sql.Tbl.checkTableCreation:before:sql=",sql);
				int r= Sql.x(sql.toString());
				tl.log(SrvltName,".Sql.Tbl.checkTableCreation:executedSql:",dtn,":returnValue=",r);
				b=an>2?(List)a.get(2):b;if(an>2)
					for(Object bo:b){
						List c=(List)bo;
						Object[]p=new Object[c.size()];
						c.toArray(p);
						vals(p);
						try {save();} catch (Exception ex) {
							tl.error(ex, SrvltName,".Sql.Tbl.checkTableCreation:insertion",c);
						} } } } catch (SQLException ex) {
				tl.error(ex, SrvltName,".Sql.Tbl.checkTableCreation:errMain:",dtn);
			}
		}//checkTableCreation
		/**where[]={col-name , param}*/
		public int count(Object[]where) throws Exception{return count(where,null,getName());}
		public static int count(Object[]where,CI[]groupBy,String name) throws Exception{
			String sql=sql(cols(Co.count),where,groupBy,null,name);//new StringBuilder("select count(*) from `").append(getName()).append("` where `").append(where[0]).append("`=").append(Co.m(where[0]).txt);//where[0]instanceof CI?m((CI)where[0]):'?');
			return Sql.q1int(sql,-1,where[0],where[1]);}
		public int maxPlus1(CI col) throws Exception{
			String sql=sql("max(`"+col+"`)+1",null,null,null);
			return Sql.q1int(sql,1);}
		public static int maxPlus1(CI col,String dbtn) throws Exception{
			String sql="SELECT max(`"+col+"`)+1 from `"+dbtn+"`";
			return Sql.q1int(sql,1);}
		// /**returns one object from the db-query*/ /**where[]={col-name , param}*/public Object obj(CI col,Object[]where) throws Exception{return Sql.q1Obj(sql(cols(col),where),where);}
		/**returns one string*/
		public String select(CI col,Object[]where) throws Exception{
			String sql=sql(cols(col),where);
			return Sql.q1Str(sql,where);}
		// /**returns one column, where:array of two elements:1st is column param, 2nd value of param*/Object[]column(CI col,Object...where) throws Exception{ return Sql.q1col(sql(cols(col),where),where[0],where[1]);}//at
		/**returns a table*/
		public Object[][]select(CI[]col,Object[]where)throws Exception{
			return Sql.Q(sql(col,where), where);}
		/**loads one row from the table*/
		Tbl load(ResultSet rs)throws Exception{return load(rs,columns());}
		/**loads one row from the table*/
		Tbl load(ResultSet rs,CI[]a)throws Exception{
			int c=0;for(CI f:a)if(f.getType().isAssignableFrom( Map.class ))
				v(f,Json.Prsr.parse( rs.getCharacterStream(++c)));
			else v(f,rs.getObject(++c));
			return this;}

		/**loads one row from the table*/
		public Tbl load(){return loadWhere(wherePK());}

		public Tbl nullify(){return nullify(columns());}
		public Tbl nullify(CI[]a){for(CI f:a)v(f,null);return this;}
		// /**loads one row from the table*/ Tbl load(){return load(pkv());}

		/**loads one row using column CI c * /
		 Tbl loadBy(CI c,Object v){
		 try{Object[]a= Sql.q1row(sql(cols(Co.all),where(c)),v);
		 vals(a);}
		 catch(Exception x){
		 TL.tl().error(x,SrvltName,".Sql.Tbl(",this,").loadBy(",c,",",v,")");
		 }
		 return loadWhere(where(c,v));}//loadBy*/

		/**loads one row based on the where clause */
		Tbl loadWhere(Object[]where){return loadWhere(where,null);}

		public CI[]loadCols(){return cols(Co.all);}// created method 2018.12.30 11:10

		/**loads one row based on the where clause and groupBy */
		Tbl loadWhere(Object[]where,CI[]groupBy){ // created method 2018.12.17 14:16
			ResultSet rs=null;
			try{rs= Sql.R( sql(loadCols(),where,groupBy),where );
				if(rs.next()){
					load(rs);
					return this;
				}
			}
			catch(Exception x){
				TL.tl().error(x,SrvltName,".Sql.Tbl(",this,").loadWhere(",where,")");
			}finally {
				close(rs);
			}return null;}//loadBy

		/**loads one row based on the where clause */
		public static Tbl loadWhere(Class<? extends Tbl>c,Object[]where){return loadWhere(c,where,null);}

		/**loads one row based on the where clause */
		public static Tbl loadWhere(Class<? extends Tbl>c,Object[]where,CI[]groupBy)
		{
			Tbl t=null;// created method 2018.12.17 14:16
			try{t=c.newInstance().loadWhere( where ,groupBy);
			}catch(Exception x){
				TL.tl().error(x,SrvltName,".Sql.Tbl(",t,").loadWhere(",c,",",where,")");
			}
			return t;
		}//loadBy
			/* > </ >
			{
				{
					{
						*/

		/**store this entity in the dbt */
		public Tbl create() throws Exception{
			CI[] cols = columns();
			StringBuilder sql = new StringBuilder( "insert into`" ).append( getName() ).append( "`( " );
			Co.generate( sql, cols );//.toString();
			sql.append( ")values(" ).append( Co.prm.txt );//Co.m(cols[0]).txt
			for ( int i = 1; i < cols.length; i++ )
				sql.append( "," ).append( Co.prm.txt );//Co.m(cols[i]).txt
			sql.append( ")" );//int x=
			Sql.X( sql.toString(), valsForSql() );
			TL.tl().log( "create", this );//log(nw?Sql.Tbl.Log.Act.New:Sql.Tbl.Log.Act.Update);
			return this;}//save


		//public Tbl update(CI...c) throws Exception{return update(c);}

		/**store this entity in the dbt , if pkv is null , this method uses the max+1 of pk-col*/
		public Tbl update(CI[]c) throws Exception{
			StringBuilder sql = new StringBuilder( "update`" )
					                    .append( getName() ).append( "` set `" )
					                    .append( c[0]).append( "`=?" );
			Object[]p=wherePK(),a=new Object[c.length+p.length/2];
			for(CI x:c)
				if(x==c[0])sql.append( " , `" ).append( x ).append( "`=?" );
			//for()

			Sql.X( sql.toString(), valsForSql() );
			TL.tl().log( "update", this );//log(nw?Sql.Tbl.Log.Act.New:Sql.Tbl.Log.Act.Update);
			return this;}//save

		/**store this entity in the dbt */
		public Tbl save() throws Exception{return save(TL.tl());}
		public Tbl save(TL tl) throws Exception{
			CI[] cols = columns();
			StringBuilder sql = new StringBuilder( "replace into`" ).append( getName() ).append( "`( " );
			Co.generate( sql, cols );//.toString();
			sql.append( ")values(" ).append( Co.prm.txt );//Co.m(cols[0]).txt
			for ( int i = 1; i < cols.length; i++ )
				sql.append( "," ).append( Co.prm.txt );//Co.m(cols[i]).txt
			sql.append( ")" );//int x=
			Sql.X( sql.toString(), valsForSql() );
			tl.log( "save", this );//log(nw?Sql.Tbl.Log.Act.New:Sql.Tbl.Log.Act.Update);
			return this;}//save

		//void log(Sql.Tbl.Log.Act act){	Map val=asMap();Integer k=(Integer)pkv();Sql.Tbl.Log.log( Sql.Tbl.Log.Entity.valueOf(getName()), k, act, val);}
		public int delete() throws SQLException{
			int x=-1;Object[]where=wherePK();
			StringBuilder b=new StringBuilder( "delete from `" )
					                .append( getName() ).append("`" );
			Co.where( b,where );
			x= Sql.X( b.toString(),where );
			return x;}

		/**retrieve from the db table all the rows that match
		 * the conditions in < where > , create an iterator
		 * , e.g.<code>for(Tbl row:query(
		 * 		Tbl.where( CI , < val > ) ))</code>*/
		public Itrtr query(Object[]where){
			Itrtr r=new Itrtr(where);
			return r;}
		public Itrtr query(String sql,Object[]where,boolean makeClones){
			return new Itrtr(sql,where,makeClones);}
		public Itrtr query(Object[]where,boolean makeClones){return query(columns(),where,null,makeClones);}
		public Itrtr query(CI[]cols,Object[]where,CI[]groupBy,boolean makeClones){//return query(sql(cols,where,groupBy),where,makeClones);}//public Itrtr query(String sql,Object[]where,boolean makeClones){
			Itrtr r=new Itrtr(sql(cols,where,groupBy),where,makeClones);
			return r;}
		public class Itrtr implements Iterator<Tbl>,Iterable<Tbl>{
			public ResultSet rs=null;public int i=0;CI[]a;boolean makeClones=false;
			public Itrtr(String sql,Object[]where,boolean makeClones){
				this.makeClones=makeClones;a=columns();
				try{rs= Sql.R(sql, where);}
				catch(Exception x){
					TL.tl().error(x,SrvltName,".Sql.Tbl(",this,").Itrtr.<init>:where=",where);
				}
			}
			public Itrtr(Object[]where){a=columns();
				try{rs= Sql.R(sql(cols(Co.all),where), where);}
				catch(Exception x){
					TL.tl().error(x,SrvltName,".Sql.Tbl(",this,").Itrtr.<init>:where=",where);
				}}
			@Override public Iterator<Tbl>iterator(){return this;}
			@Override public boolean hasNext(){boolean b=false;
				try {b = rs!=null&&rs.next();} catch (SQLException x) {
					TL.tl().error(x,SrvltName,".Sql.Tbl(",this,").Itrtr.hasNext:i=",i,",rs=",rs);
				}
				if(!b&&rs!=null){
					Sql.close(rs);rs=null;}
				return b;}
			@Override public Tbl next(){i++;Tbl t=Tbl.this;TL tl=TL.tl();
				if(makeClones)try{
					t=t.getClass().newInstance();}catch(Exception ex){
					tl.error(ex,SrvltName,".Sql.Tbl(",this,").Itrtr.next:i=",i,":",rs,":makeClones");
				}
				try{t.load(rs,a);}catch(Exception x){
					tl.error(x,SrvltName,".Sql.Tbl(",this,").Itrtr.next:i=",i,":",rs);
					close(rs,tl);rs=null;
				}
				return t;}
			@Override public void remove(){throw new UnsupportedOperationException();}
		}//Itrtr
		/**Class for Utility methods on set-of-columns, opposed to operations on a single column*/
		public enum Co implements CI {//Marker ,sql-preparedStatement-parameter
			all("*")
			,prm("?")
			,Null("null")
			,distinct("distinct")
			,password("password(?)")
			,lt("<"),le("<="),ne("<>"),gt(">"),ge(">=")
			,or("or"),like("like"),in("in"),and("and")//,prnthss("("),max("max(?)")
			//public enum Func{
			,now("now()")
			,uuid("uuid()")
			,count("count(*)")
			,max("max(") //added 2018.12.17 10:58
			//,min(,avg(,}
			;
			String txt;boolean paranthesis;
			Co(String p){txt=p;paranthesis=p.endsWith( "(" );}
			@Override public Field f(){return null;}
			@Override public String getName(){return name();}
			@Override public Class getType(){return String.class;}

			public static Field f(String name,Class<? extends Tbl>c){
				//for(Field f:fields(c))if(name.equals(f.getName()))return f;return null;
				Field r=null;try{r=c.getField(name);}catch(Exception x) {
					TL.tl().error(x,SrvltName,".Sql.Tbl.f(",name,c,"):");
				}
				return r;
			}

			/**generate Sql into the StringBuilder*/
			public static StringBuilder generate(StringBuilder b,CI[]col){ return generate(b,col,",");}

			static StringBuilder generate(StringBuilder b,CI[]col,String separator){
				if(separator==null)separator=",";
				for(int n=col.length,i=0;i<n;i++){
					if(i>0)b.append(separator);
					if(col[i] instanceof Co)
					{	Co c=(Co)col[i];
						b.append(c.txt);
						if(c ==Co.distinct && i+1<n)
							b.append(" `").append(col[++i]).append("`");
						else if(c.paranthesis && i+1<n)
							b.append("`").append(col[++i]).append("`)");
					}else
						b.append("`").append(col[i]).append("`");}
				return b;}

			public static StringBuilder genList(StringBuilder b,List l){
				b.append(" (");boolean comma=false;
				for(Object z:l){
					if(comma)b.append( ',' );else comma=true;
					if(z instanceof Number)
						b.append( z );else
						b.append( '\'' ).append(
								(z instanceof String?(String)z:z.toString()
								).replaceAll( "'","''" )
						)
								.append( '\'' );
				}b.append(")");
				return b;}

			static StringBuilder where(StringBuilder b,Object[]where){
				if(where==null || where.length<1)return b;
				b.append(" where ");
				for(int n=where.length,i=0;i<n;i+=2){Object o=where[i];
					if(i>0)b.append(" and ");
					where(b,o,i+1<n ?where[i+1]:null);//i++;
				}//for //where(b,Co.and,where);
				return b;}

			/**
			 * in the case of Co.max , o should be CI, and o1 is Co.max
			 *
			 * in the case of Co.and and Co.or
			 * the even-prm is Co.or or Co.and , and the odd-prm is a list
			 * */
			static StringBuilder where(StringBuilder b,Object o,Object o1){
				if(o==null )return b;
				if(o1==Co.max && o instanceof CI) //added 2018.12.17 10:58
					b.append("max(`").append(o).append("`)=`").append(o).append("`");
				else if((o==Co.and || o==Co.or )&& o1 instanceof List){
					List l=(List)o1;int c=0;b.append( '(' );
					for(Object e:l){
						if(c++>0)//b.append( " or " );
							b.append( ' ' ).append( o ).append( ' ' );
						where(b,e,c<l.size() ?l.get( c ):null);
					}b.append( ')' );
				}else
				if(o instanceof Co)b.append(o);else
				if(o instanceof CI)
					b.append('`').append(o).append("`=")
							.append('?');//Co.m(o).txt
				else if(o instanceof List){List l=(List)o;
					o=l.size()>1?l.get(1):null;
					if(o ==Co.in && o1 instanceof List){
						b.append('`').append(l.get(0)).append("` ").append(o);
						l=(List)o1;
						genList(b,l);
					}else if(o instanceof Co)//o!=null)//if(ln==2 && )
					{	Co m=(Co)o;o=l.get(0);
						if(o instanceof CI || o instanceof Co)
							b.append('`').append(o).append('`');
						else
							TL.tl().log(SrvltName,".Sql.Tbl.Co.where:unknown where-clause item:o=",o);
						b.append(m.txt).append("?");
					}else
						TL.tl().log(SrvltName,".Sql.Tbl.Co.where:unknown where-clause item: o=",o);
				}
				else TL.tl().error(null,SrvltName,".Sql.Tbl.Col.where:for:",o);
				return b;}
		}//enum Co

		/**output to jspOut one row of json of this row*/
		public void outputJson(){try{TL.tl().getOut().o(this);}catch(IOException x){
			TL.tl().error(x,"moh.Sql.Tbl.outputJson:IOEx:");
		}}
		/**output to jspOut rows of json that meet the 'where' conditions*/
		public void outputJson(Object...where){try{
			Json.Output o=TL.tl().getOut();
			o.w('[');boolean comma=false;
			for(Tbl i:query(where)){
				if(comma)o.w(',');else comma=true;
				i.outputJson();}
			o.w(']');
		} catch (IOException e){
			TL.tl().error(e,SrvltName,".Sql.Tbl.outputJson:");
		}
		}//outputJson(Object...where)
		public static List<Class<? extends Tbl>>registered=new LinkedList<Class<? extends Tbl>>(); // </ > </ >
		static void check(TL tl){
			for(Class<? extends Tbl>c:registered)try
			{String n=c.getName(),n2=SrvltName+".checkDBTCreation."+n;
				if( tl.h.a(n2)==null){
					Tbl t=c.newInstance();
					t.checkDBTCreation(tl);
					tl.h.a(n2,tl.now);
				}}catch(Exception ex){
				tl.error( ex,SrvltName,".Sql.Tbl.check" );
			} }/* </ > </x >
			{
				{
					{
			*/

		public static boolean exists(Object[]where,String dbtName){return exists(where,null,dbtName);}

		public static boolean exists(Object[]where,CI[]groupBy,String dbtName){
			boolean b=false;
			int n=0;
			try{n=count( where,groupBy,dbtName );}catch ( Exception ex ){}
			b=n>0;
			return b;
		}
	}//class Tbl
}//class Sql

//////////////////////////////////////////////////////////////////////


static class Json{
	public static class Output
	{ public interface I{ public Json.Output jsonOutput( Json.Output o, String ind, String path ) throws IOException ;}

		public Writer w;
		public boolean initCache=false,includeObj=false,comment=false;
		Map<Object, String> cache;
		public static void out(Object o,Writer w,boolean initCache,boolean includeObj)
				throws IOException{Json.Output t=new Json.Output(w,initCache,includeObj);t.o(o);if(t.cache!=null){t.cache.clear();t.cache=null;}}
		public static String out(Object o,boolean initCache,boolean includeObj){StringWriter w=new StringWriter();
			try{out(o,w,initCache,includeObj);}catch(Exception ex){TL.tl().log("Json.Output.out",ex);}return w.toString();}
		public static String out(Object o){StringWriter w=new StringWriter();try{out(o,w,
				false,false);}catch(Exception ex){TL.tl().log("Json.Output.out",ex);}return w.toString();}
		public Output(){w=new StringWriter();}
		public Output(Writer p){w=p;}
		public Output(Writer p,boolean initCache,boolean includeObj)
		{w=p;this.initCache=initCache;this.includeObj=includeObj;}
		public Output(boolean initCache,boolean includeObj){this(new StringWriter(),initCache,includeObj);}
		public Output(String p)throws IOException{w=new StringWriter();w(p);}
		public Output(OutputStream p)throws Exception{w=new OutputStreamWriter(p);}
		public String toString(){return w==null?null:w.toString();}
		public String toStrin_(){String r=w==null?null:w.toString();clrSW();return r;}
		public Output w(char s)throws IOException{if(w!=null)w.write(s);return this;}
		public Output w(String s)throws IOException{if(w!=null)w.write(s);return this;}
		public Output p(String s)throws IOException{return w(s);}
		public Output p(char s)throws IOException{return w(s);}
		public Output p(long s)throws IOException{return w(String.valueOf(s));}
		public Output p(int s)throws IOException{return w(String.valueOf(s));}
		public Output p(boolean s)throws IOException{return w(String.valueOf(s));}
		public Output o(Object...a)throws IOException{return o("","",a);}
		public Output o(Object a,String indentation)throws IOException{return o(a,indentation,"");}
		public Output o(String ind,String path,Object[]a)throws IOException
		{for(Object i:a)o(i,ind,path);return this;}
		public Output o(Object a,String ind,String path)throws IOException
		{if(cache!=null&&a!=null&&((!includeObj&&path!=null&&path.length()<1)||cache.containsKey(a)))
		{Object p=cache.get(a);if(p!=null){o(p.toString());o("/*cacheReference*/");return this;}}
			final boolean c=comment;
			if(a==null)w("null"); //Object\n.p(ind)
			else if(a instanceof String)oStr(String.valueOf(a),ind);
			else if(a instanceof Boolean||a instanceof Number)w(a.toString());
			else if(a instanceof Json.Output.I)((Json.Output.I)a).jsonOutput(this,ind,path);//oDbTbl((Sql.Tbl)a,ind,path);
				//else if(a instanceof Sql.Tbl)((Sql.Tbl)a).jsonOutput(this,ind,path);//oDbTbl((Sql.Tbl)a,ind,path);
			else if(a instanceof Map<?,?>)oMap((Map)a,ind,path);
			else if(a instanceof Collection<?>)oCollctn((Collection)a,ind,path);
			else if(a instanceof Object[])oArray((Object[])a,ind,path);
			else if(a.getClass().isArray())oarray(a,ind,path);
			else if(a instanceof java.util.Date)oDt((java.util.Date)a,ind);
			else if(a instanceof Iterator<?>)oItrtr((Iterator)a,ind,path);
			else if(a instanceof Enumeration<?>)oEnumrtn((Enumeration)a,ind,path);
			else if(a instanceof Throwable)oThrbl((Throwable)a,ind);
			else if(a instanceof ResultSet)oResultSet(( ResultSet)a,ind,path);
			else if(a instanceof ResultSetMetaData)oResultSetMetaData((ResultSetMetaData)a,ind,path);
			else if(a instanceof TL)oTL((TL)a,ind,path);
			else if(a instanceof ServletContext)oSC((ServletContext)a,ind,path);
			else if(a instanceof ServletConfig )oSCnfg((ServletConfig)a,ind,path);
			else if(a instanceof HttpServletRequest)oReq((HttpServletRequest)a,ind,path);
			else if(a instanceof HttpSession)oSession((HttpSession)a,ind,path);
			else if(a instanceof Cookie )oCookie((Cookie)a,ind,path);
			else if(a instanceof java.util.UUID)w("\"").p(a.toString()).w(c?"\"/*uuid*/":"\"");
			else{w("{\"class\":").oStr(a.getClass().getName(),ind)
					     .w(",\"str\":").oStr(String.valueOf(a),ind)
					     .w(",\"hashCode\":").oStr(Long.toHexString(a.hashCode()),ind);
				if(c)w("}//Object&cachePath=\"").p(path).w("\"\n").p(ind);
				else w("}");}return this;}

		public Output oStr(String a,String indentation)throws IOException
		{final boolean m=comment;if(a==null)return w(m?" null //String\n"+indentation:"null");
			w('"');for(int n=a.length(),i=0;i<n;i++)
		{char c=a.charAt(i);if(c=='\\')w('\\').w('\\');
		else if(c=='"')w('\\').w('"');
		else if(c=='\n'){w('\\').w('n');if(m)w("\"\n").p(indentation).w("+\"");}
		else if(c=='\r')w('\\').w('r');
		else if(c=='\t')w('\\').w('t');
		else if(c=='\'')w('\\').w('\'');
		else p(c);}return w('"');}
		public Output oDt(java.util.Date a,String indentation)throws IOException
		{if(a==null)return w(comment?" null //Date\n":"null");
			//w("{\"class\":\"Date\",\"time\":0x").p(Long.toHexString( a.getTime()));//.w(",\"str\":").oStr(a.toString(),indentation);
			w("0x").p(Long.toHexString( a.getTime()));//if(comment)w("}//Date\n").p(indentation);else w("}");
			return this;}
		public Output oThrbl(Throwable x,String indentation)throws IOException
		{w("{\"message\":").oStr(x.getMessage(),indentation).w(",\"stackTrace\":");
			try{StringWriter sw=new StringWriter();
				x.printStackTrace(new PrintWriter(sw));
				oStr(sw.toString(),indentation);}catch(Exception ex)
			{TL.tl().log("Json.Output.x("+x+"):",ex);}return w("}");}
		public Output oEnumrtn(Enumeration a,String ind,String path)throws IOException
		{final boolean c=comment;
			if(a==null)return c?w(" null //Enumeration\n").p(ind):w("null");
			boolean comma=false;String i2=c?ind+"\t":ind;
			if(c)w("[//Enumeration\n").p(ind);else w("[");
			if(c&&path==null)path="";if(c&&path.length()>0)path+=".";int i=0;
			while(a.hasMoreElements()){if(comma)w(" , ");else comma=true;
				o(a.nextElement(),i2,c?path+(i++):path);}
			return c?w("]//Enumeration&cachePath=\"").p(path).w("\"\n").p(ind):w("]");}
		public Output oItrtr(Iterator a,String ind,String path)throws IOException
		{final boolean c=comment;if(a==null)return c?w(" null //Iterator\n").p(ind):w("null");
			boolean comma=false;String i2=c?ind+"\t":ind;
			if(c){w("[//").p(a.toString()).w(" : Itrtr\n").p(ind);
				if(path==null)path="";if(path.length()>0)path+=".";}
			else w("[");int i=0;
			while(a.hasNext()){if(comma)w(" , ");else comma=true;o(a.next(),i2,c?path+(i++):path);}
			return c?w("]//Iterator&cachePath=\"").p(path).w("\"\n").p(ind):w("]");}
		public Output oArray(Object[]a,String ind,String path)throws IOException
		{final boolean c=comment;
			if(a==null)return c?w(" null //array\n").p(ind):w("null");
			String i2=c?ind+"\t":ind;
			if(c){w("[//array.length=").p(a.length).w("\n").p(ind);
				if(path==null)path="";if(path.length()>0)path+=".";}else w("[");
			for(int i=0;i<a.length;i++){if(i>0)w(" , ");o(a[i],i2,c?path+i:path);}
			return c?w("]//cachePath=\"").p(path).w("\"\n").p(ind):w("]");}
		public Output oarray(Object a,String ind,String path)throws IOException
		{final boolean c=comment;
			if(a==null)return c?w(" null //array\n").p(ind):w("null");
			int n= java.lang.reflect.Array.getLength(a);String i2=c?ind+"\t":ind;
			if(c){w("[//array.length=").p(n).w("\n").p(ind);
				if(path==null)path="";if(path.length()>0)path+=".";}else w("[");
			for(int i=0;i<n;i++){if(i>0)w(" , ");o( java.lang.reflect.Array.get(a,i),i2,c?path+i:path);}
			return c?w("]//cachePath=\"").p(path).w("\"\n").p(ind):w("]");}
		public Output oCollctn(Collection o,String ind,String path)throws IOException
		{if(o==null)return w("null");final boolean c=comment;
			if(c){w("[//").p(o.getClass().getName()).w(":Collection:size=").p(o.size()).w("\n").p(ind);
				if(cache==null&&initCache)cache=new HashMap<Object, String>();
				if(cache!=null)cache.put(o,path);
				if(c&&path==null)path="";if(c&&path.length()>0)path+=".";
			}else w("[");
			Iterator e=o.iterator();int i=0;
			if(e.hasNext()){o(e.next(),ind,c?path+(i++):path);
				while(e.hasNext()){w(",");o(e.next(),ind,c?path+(i++):path);}}
			return c?w("]//").p(o.getClass().getName()).w("&cachePath=\"").p(path).w("\"\n").p(ind) :w("]");}
		public Output oMap(Map o,String ind,String path) throws IOException
		{if(o==null)return w("null");final boolean c=comment;
			if(c){w("{//").p(o.getClass().getName()).w(":Map\n").p(ind);
				if(cache==null&&initCache)cache=new HashMap<Object, String>();
				if(cache!=null)cache.put(o,path);}else w("{");
			Iterator e=o.keySet().iterator();Object k,v;
			//if(o instanceof Store.JsonStorage)w("uuid:").o(((Store.JsonStorage)o).uuid);
			if(e.hasNext()){k=e.next();v=o.get(k);//if(o instanceof Store.JsonStorage)w(",");
				o(k,ind,c?path+k:path);w(":");o(v,ind,c?path+k:path);}
			while(e.hasNext()){k=e.next();v=o.get(k);w(",");
				o(k,ind,c?path+k:path);w(":");o(v,ind,c?path+k:path);}
			if(c) w("}//")
					      .p(o.getClass().getName())
					      .w("&cachePath=\"")
					      .p(path)
					      .w("\"\n")
					      .p(ind);else w("}");
			return this;}
		public Output oReq(HttpServletRequest r,String ind,String path)throws IOException
		{final boolean c=comment;try{boolean comma=false,c2;//,d[]
			String k,i2=c?ind+"\t":ind,ct;int j;Enumeration e,f;
			(c?w("{//").p(r.getClass().getName()).w(":HttpServletRequest\n").p(ind):w("{"))
					.w("\"dt\":").oDt(TL.tl().now,i2)//new java.util.Date()
					.w(",\"AuthType\":").o(r.getAuthType(),i2,c?path+".AuthTyp":path)
					.w(",\"CharacterEncoding\":").o(r.getCharacterEncoding(),i2,c?path+".CharacterEncoding":path)
					.w(",\"ContentLength\":").o(r.getContentLength(),i2,c?path+".ContentLength":path)
					.w(",\"ContentType\":").o(ct=r.getContentType(),i2,c?path+".ContentType":path)
					.w(",\"ContextPath\":").o(r.getContextPath(),i2,c?path+".ContextPath":path)
					.w(",\"Method\":").o(r.getMethod(),i2,c?path+".Method":path)
					.w(",\"PathInfo\":").o(r.getPathInfo(),i2,c?path+".PathInfo":path)
					.w(",\"PathTranslated\":").o(r.getPathTranslated(),i2,c?path+".PathTranslated":path)
					.w(",\"Protocol\":").o(r.getProtocol(),i2,c?path+".Protocol":path)
					.w(",\"QueryString\":").o(r.getQueryString(),i2,c?path+".QueryString":path)
					.w(",\"RemoteAddr\":").o(r.getRemoteAddr(),i2,c?path+".RemoteAddr":path)
					.w(",\"RemoteHost\":").o(r.getRemoteHost(),i2,c?path+".RemoteHost":path)
					.w(",\"RemoteUser\":").o(r.getRemoteUser(),i2,c?path+".RemoteUser":path)
					.w(",\"RequestedSessionId\":").o(r.getRequestedSessionId(),i2,c?path+".RequestedSessionId":path)
					.w(",\"RequestURI\":").o(r.getRequestURI(),i2,c?path+".RequestURI":path)
					.w(",\"Scheme\":").o(r.getScheme(),i2,c?path+".Scheme":path)
					.w(",\"UserPrincipal\":").o(r.getUserPrincipal(),i2,c?path+".UserPrincipal":path)
					.w(",\"Secure\":").o(r.isSecure(),i2,c?path+".Secure":path)
					.w(",\"SessionIdFromCookie\":").o(r.isRequestedSessionIdFromCookie(),i2,c?path+".SessionIdFromCookie":path)
					.w(",\"SessionIdFromURL\":").o(r.isRequestedSessionIdFromURL(),i2,c?path+".SessionIdFromURL":path)
					.w(",\"SessionIdValid\":").o(r.isRequestedSessionIdValid(),i2,c?path+".SessionIdValid":path)
					.w(",\"Locales\":").oEnumrtn(r.getLocales(),ind,c?path+".Locales":path)
					.w(",\"Attributes\":{");
			comma=false;
			e=r.getAttributeNames();while(e.hasMoreElements())
				try{k=e.nextElement().toString();if(comma)w(",");else comma=true;
					o(k).w(":").o(r.getAttribute(k),i2,c?path+"."+k:path);
				}catch(Throwable ex){TL.tl().error(ex,"HttpRequestToJsonStr:attrib");}
			w("}, \"Headers\":{");comma=false;e=r.getHeaderNames();
			while(e.hasMoreElements())try
			{k=e.nextElement().toString();
				if(comma)w(",");else comma=true;o(k).w(":[");
				f=r.getHeaders(k);c2=false;j=-1;while(f.hasMoreElements())
			{if(c2)w(",");else c2=true;o(f.nextElement(),i2,c?path+".Headers."+k+"."+(++j):path);}
				w("]");
			}catch(Throwable ex){TL.tl().error(ex,"Json.Output.oReq:Headers");}
			w("}, \"Parameters\":").oMap(r.getParameterMap(),i2,c?path+".Parameters":path)
					.w(",\"Session\":").o(r.getSession(false),i2,c?path+".Session":path)
					.w(", \"Cookies\":").o(r.getCookies(),i2,c?path+".Cookies":path);
			//if(ct!=null&&ct.indexOf("part")!=-1)w(", \"Parts\":").o(r.getParts(),i2,c?path+".Parts":path);
			//AsyncContext =r.getAsyncContext();
			//long =r.getDateHeader(arg0)
			//DispatcherType =r.getDispatcherType()
			//String =r.getLocalAddr()
			//String =r.getLocalName()
			//int =r.getLocalPort()
			//int =r.getRemotePort()
			//RequestDispatcher =r.getRequestDispatcher(String)
			//StringBuffer r.getRequestURL()
			//String r.getServerName()
			//int r.getServerPort()
			//ServletContext =r.getServletContext()
			//String r.getServletPath()
			//boolean r.isAsyncStarted()
			//boolean r.isAsyncSupported()
			//boolean r.isUserInRole(String)
		}catch(Exception ex){TL.tl().error(ex,"Json.Output.oReq:Exception:");}
			if(c)w("}//").p(r.getClass().getName()).w("&cachePath=\"").p(path).w("\"\n").p(ind);
			else w("}");
			return this;}
		Output oSession(HttpSession s,String ind,String path)throws IOException
		{final boolean c=comment;try{if(s==null)w("null");else
		{String i2=c?ind+"\t":ind;
			(c?w("{//").p(s.getClass().getName()).w(":HttpSession\n").p(ind):w("{"))
					.w("{\"isNew\":").p(s.isNew()).w(",sid:").oStr(s.getId(),ind)
					.w(",\"CreationTime\":").p(s.getCreationTime())
					.w(",\"MaxInactiveInterval\":").p(s.getMaxInactiveInterval())
					.w(",\"attributes\":{");Enumeration e=s.getAttributeNames();boolean comma=false;
			while(e.hasMoreElements())
			{Object k=e.nextElement().toString();if(comma)w(",");else comma=true;
				o(k,i2).w(":").o(s.getAttribute(String.valueOf(k)),i2,c?path+".Attributes."+k:path);
			}w("}");}}catch(Exception ex){TL.tl().error(ex,"Json.Output.Session:");}
			if(c)w("}//").p(s.getClass().getName()).w("&cachePath=\"").p(path).w("\"\n").p(ind);
			else w("}");
			return this;}
		public Output oCookie(Cookie y,String ind,String path)throws IOException
		{final boolean c=comment;try{(c?w("{//")
				                                .p(y.getClass().getName()).w(":Cookie\n").p(ind):w("{"))
				                             .w("\"Comment\":").o(y.getComment())
				                             .w(",\"Domain\":").o(y.getDomain())
				                             .w(",\"MaxAge\":").p(y.getMaxAge())
				                             .w(",\"Name\":").o(y.getName())
				                             .w(",\"Path\":").o(y.getPath())
				                             .w(",\"Secure\":").p(y.getSecure())
				                             .w(",\"Version\":").p(y.getVersion())
				                             .w(",\"Value\":").o(y.getValue());
		}catch(Exception ex){TL.tl().error(ex,"Json.Output.Cookie:");}
			if(c)try{w("}//").p(y.getClass().getName()).w("&cachePath=\"").p(path).w("\"\n").p(ind);
			}catch(Exception ex){TL.tl().error(ex,"Json.Output.Cookie:");}else w("}");
			return this;}

		Output oTL(TL y,String ind,String path)throws IOException
		{final boolean c=comment;try{String i2=c?ind+"\t":ind;
			(c?w("{//").p(y.getClass().getName()).w(":PageContext\n").p(ind):w("{"))
					.w("\"ip\":").o(y.h.ip,i2,c?path+".ip":path)
					.w(",\"usr\":").o(y.usr,i2,c?path+".usr":path)

					.w(",\"logOut\":").o(y.h.logOut,i2,c?path+".logOut":path)
					.w(",\"urli\":").o(y.h.urli,i2,c?path+".urli":path)
					.w(",\"url\":").o(y.h.url,i2,c?path+".url":path)
					.w(",\"mth\":").o(mth,i2,c?path+".mth":path)

					.w(",\"now\":").o(y.now,i2,c?path+".now":path)
					.w(",\"json\":").o(y.json,i2,c?path+".json":path)
					//.w(",\"response\":").o(y.response,i2,c?path+".response":path)
					.w(",\"Request\":").o(y.h.getRequest(),i2,c?path+".request":path)
					//.w(",\"Session\":").o(y.getSession(false))
					.w(",\"application\":").o(y.h.getServletContext(),i2,c?path+".application":path)
			//.w(",\"config\":").o(y.req.getServletContext().getServletConfig(),i2,c?path+".config":path)
			//.w(",\"Page\":").o(y.srvlt,i2,c?path+".Page":path)
			//.w(",\"Response\":").o(y.rspns,i2,c?path+".Response":path)
			;
		}catch(Exception ex){TL.tl().error(ex,"Json.Output.oTL:");}
			if(c)try{w("}//").p(y.getClass().getName()).w("&cachePath=\"").p(path).w("\"\n").p(ind);}
			catch(Exception ex){TL.tl().error(ex,"Json.Output.oTL:closing:");}
			else w("}");
			return this;}

		Output oSC(ServletContext y,String ind,String path)
		{final boolean c=comment;try{String i2=c?ind+"\t":ind;(c?w("{//").p(y.getClass().getName()).w(":ServletContext\n").p(ind):w("{"))
				                                                      .w(",\"ContextPath\":").o(y.getContextPath(),i2,c?path+".ContextPath":path)
				                                                      .w(",\"MajorVersion\":").o(y.getMajorVersion(),i2,c?path+".MajorVersion":path)
				                                                      .w(",\"MinorVersion\":").o(y.getMinorVersion(),i2,c?path+".MinorVersion":path);
			if(c)
				w("}//").p(y.getClass().getName()).w("&cachePath=\"").p(path).w("\"\n").p(ind);
			else w("}");
		}catch(Exception ex){TL.tl().error(ex,"Json.Output.ServletContext:");}
			return this;}

		Output oSCnfg(ServletConfig y,String ind,String path)throws IOException
		{final boolean c=comment;try{if(c)w("{//").p(y.getClass().getName()).w(":ServletConfiguration\n").p(ind);
		else w("{");
			//String getInitParameter(String)
			//Enumeration getInitParameterNames()
			//getServletContext()
			//String getServletName()	.w(",:").o(y.(),i2,c?path+".":path)
		}catch(Exception ex){TL.tl().error(ex,"Json.Output.ServletConfiguration:");}
			return c?w("}//").p(y.getClass().getName()).w("&cachePath=\"").p(path).w("\"\n").p(ind) :w("}");}
		Output oBean(Object o,String ind,String path)
		{final boolean c=comment;try{String i2=c?ind+"\t":ind,i3=c?i2+"\t":ind;Class z=o.getClass();
			(c?w("{//").p(z.getName()).w(":Bean\n").p(ind):w("{"))
					.w("\"str\":").o(o.toString(),i2,c?path+".":path)
//		.w(",:").o(o.(),i2,c?path+".":path)
			;Method[]a=z.getMethods();//added 2015.11.21
			for(Method m:a){String n=m.getName();
				if(n.startsWith("get")&&m.getParameterTypes().length==0)//.getParameterCount()
					w("\n").w(i2).w(",").p(n).w(':').o(m.invoke(o), i3, path+'.'+n);}
			if(c)w("}//").p(o.getClass().getName()).w("&cachePath=\"").p(path).w("\"\n").p(ind);
			else w("}");}catch(Exception ex){TL.tl().error(ex,"Json.Output.Bean:");}return this;}
		Output oResultSet(ResultSet o,String ind,String path)
		{final boolean c=comment;try{String i2=c?ind+"\t":ind;
			Sql.ItTbl it=new Sql.ItTbl(o);
			(c?w("{//").p(o.getClass().getName()).w(":ResultSet\n").p(ind):w("{"))
					.w("\"h\":").oResultSetMetaData(it.row.m,i2,c?path+".h":path)
					.w("\n").p(ind).w(",\"a\":").o(it,i2,c?path+".a":path);
			if(c)w("}//").p(o.getClass().getName()).w("&cachePath=\"").p(path).w("\"\n").p(ind);
			else w("}");}catch(Exception ex){TL.tl().error(ex,"Json.Output.ResultSet:");}return this;}
		Output oResultSetMetaData(ResultSetMetaData o,String ind,String path)
		{final boolean c=comment;try{String i2=c?ind+"\t":ind;
			int cc=o.getColumnCount();
			if(c)w("[//").p(o.getClass().getName()).w(":ResultSetMetaData\n").p(ind);
			else w("[");
			for(int i=1;i<=cc;i++){
				if(i>1){if(c)w("\n").p(i2).w(",");else w(",");}
				w("{\"name\":").oStr(o.getColumnName( i ),i2)
						.w(",\"label\":").oStr(o.getColumnLabel( i ),i2)
						.w(",\"width\":").p(o.getColumnDisplaySize( i ))
						.w(",\"className\":").oStr(o.getColumnClassName( i ),i2)
						.w(",\"type\":").oStr(o.getColumnTypeName( i ),i2).w("}");
			}//for i<=cc
			if(c)w("]//").p(o.getClass().getName()).w("&cachePath=\"").p(path).w("\"\n").p(ind);
			else w("]");}catch(Exception ex){TL.tl().error(ex,"Json.Output.ResultSetMetaData:");}return this;}
		public Output clrSW(){if(w instanceof StringWriter){((StringWriter)w).getBuffer().setLength(0);}return this;}
		public Output flush() throws IOException{w.flush();return this;}
	} //class Output


	public static class Prsr {

		public StringBuilder buff=new StringBuilder() ,lookahead=new StringBuilder();
		public Reader rdr;

		public String comments=null;
		public char c;Map<String,Object>cache=null;

		enum Literal{Undefined,Null};//,False,True

		public static Object parse(String p)throws Exception{
			return parse(new java.io.StringReader(p));}

		public static Object parse(HttpServletRequest p)throws Exception{
			return parse(p.getReader());}

		public static Object parse(Reader p)throws Exception{
			if(p==null)return null;
			Prsr j=new Prsr();j.rdr=p;j.nxt(j.c=j.read());
			return j.parse();}//public static Object parseItem(Reader p)throws Exception{ Prsr j=new Prsr();j.rdr=p;j.nxt(j.c=j.read());return j.parseItem();}

		/**skip Redundent WhiteSpace*/void skipRWS(){
			boolean b=Character.isWhitespace(c);
			while(b && c!='\0'){
				char x=peek();
				if(b=Character.isWhitespace(x))
					nxt();
			}
		}

		void skipRWSx(char...p){
			skipRWS();
			char x=peek();int i=-1,n=p==null?0:p.length;boolean b=false;
			do{
				if((b=++i<n)&&p[i]==x){
					b=false;nxt();
				}
			}while(b);
		}// boolean chk(){boolean b=Character.isWhitespace(c)||c=='/';while(b && c!='\0'){//Character.isWhitespace(c)||)char x=peek();if(c=='/' &&(lookahead("//") || lookahead("/*"))){	skipWhiteSpace();b=Character.isWhitespace(c);}else if(x=='/' &&(lookahead(x+"//") || lookahead(x+"/*") )){}else{	if(b=Character.isWhitespace(x))nxt();}}return false;}

		public Object parse()throws Exception{
			Object r=c!='\0'?parseItem():null;
			skipWhiteSpace();if(c!='\0')
			{LinkedList l=new LinkedList();l.add(r);
				while(c!='\0'){
					r=parseItem();
					l.add(r);
				}r=l;}
			return r;}

		public Object parseItem()throws Exception{
			Object r=null;int i;skipWhiteSpace();switch(c)
			{ case '"':case '`':case '\'':r=extractStringLiteral();break;
				case '0':case '1':case '2':case '3':case '4':
				case '5':case '6':case '7':case '8':case '9':
				case '-':case '+':case '.':r=extractDigits();break;
				case '[':r=extractArray();break;
				case '{':Map m=extractObject();
					r=m==null?null:m.get("class");
					if(r instanceof String && "date".equalsIgnoreCase((String)r)){
						r=m.get("time");
						r=new Date(((Number)r).longValue());}
					else r=m;break;
				case '(':nxt();
				{
					skipRWS();//skipWhiteSpace();
					r=parseItem();
					skipWhiteSpace();
					if(c==')')
						nxt();
					else{LinkedList l=new LinkedList();
						l.add(r);
						while(c!=')' && c!='\0'){
							r=parseItem();
							l.add(r);
							skipWhiteSpace();
						}if(c==')')
							nxt();
						r=l;}}break;
				default:r=extractIdentifier();
			}skipRWS();//skipWhiteSpace();
			if(comments!=null&&((i=comments.indexOf("cachePath=\""))!=-1
					                    ||(cache!=null&&comments.startsWith("cacheReference"))))
			{	if(i!=-1)
			{	if(cache==null)
				cache=new HashMap<String,Object>();
				int j=comments.indexOf("\"",i+=11);
				cache.put(comments.substring(i,j!=-1?j:comments.length()),r);
			}else
				r=cache.get(r);
				comments=null;
			}
			return r;}

		public String extractStringLiteral()throws Exception{
			char first=c;nxt();boolean b=c!=first&&c!='\0';
			while(b)
			{if(c=='\\'){nxt();switch(c)
			{case 'n':buff('\n');break;case 't':buff('\t');break;
				case 'r':buff('\r');break;case '0':buff('\0');break;
				case 'x':case 'X':buff( (char)
						                        java.lang.Integer.parseInt(
								                        next(2)//p.substring(offset,offset+2)
								                        ,16));nxt();//next();
				break;
				case 'u':
				case 'U':buff( (char)
						               java.lang.Integer.parseInt(
								               next(4)//p.substring(offset,offset+4)
								               ,16));//next();next();next();//next();
					break;default:if(c!='\0')buff(c);}}
			else buff(c);
				nxt();b=c!=first&&c!='\0';
			}if(c==first)nxt();return consume();}

		public Object extractIdentifier(){
			while(!Character.isUnicodeIdentifierStart(c))
			{System.err.println("unexpected:"+c+" at row,col="+rc());nxt();return Literal.Null;}
			bNxt();
			while(c!='\0'&&Character.isUnicodeIdentifierPart(c))bNxt();
			String r=consume();
			return "true".equals(r)?new Boolean(true)
					       :"false".equals(r)?new Boolean(false)
							        :"null".equals(r)?Literal.Null
									         :"undefined".equals(r)?Literal.Undefined
											          :r;}

		public Object extractDigits(){
			if(c=='0')//&&offset+1<len)
			{char c2=peek();if(c2=='x'||c2=='X')
			{nxt();nxt();
				while((c>='A'&&c<='F')
						      ||(c>='a'&&c<='f')
						      ||Character.isDigit(c))bNxt();
				String s=consume();
				try{return Long.parseLong(s,16);}
				catch(Exception ex){}return s;}
			}boolean dot=c=='.';
			bNxt();//if(c=='-'||c=='+'||dot)bNxt();else{c=p.charAt(i);}
			while(c!='\0'&&Character.isDigit(c))bNxt();
			if(!dot&&c=='.'){dot=true;bNxt();}
			if(dot){while(c!='\0'&&Character.isDigit(c))bNxt();}
			if(c=='e'||c=='E')
			{dot=false;bNxt();if(c=='-'||c=='+')bNxt();
				while(c!='\0'&&Character.isDigit(c))bNxt();
			}else if(c=='l'||c=='L'||c=='d'||c=='D'||c=='f'||c=='F')bNxt();
			String s=consume();//p.substring(i,offset);
			if(!dot)try{return Long.parseLong(s);}catch(Exception ex){}
			try{return Double.parseDouble(s);}catch(Exception ex){}return s;}

		public List<Object> extractArray()throws Exception{
			if(c!='[')return null;
			nxt();char x=0;
			LinkedList<Object> l=new LinkedList<Object>();
			Object r=null;
			skipWhiteSpace();
			if(c!='\0'&&c!=']')
			{	r=parseItem();
				l.add(r);
			}if(c!='\0'&&c!=']')
				skipRWSx(']',',');//skipRWS();x=peek();if(x==']'||x==',') nxt();//skipWhiteSpace();
			while(c!='\0'&&c!=']')
			{	if(c!=','&&!Character.isWhitespace(c))//throw new IllegalArgumentException
				System.out.println("Array:"+rc()+" expected ','");
				nxt();
				r=parseItem();
				l.add(r);
				skipRWSx(']',',');//skipRWS();x=peek();if(x==']'||x==',')nxt();//skipWhiteSpace();
			}if(c==']')
				nxt();
			skipRWS();
			return l;}

		public Map<Object,Object> extractObject()throws Exception{
			final char bo='{',bc='}';
			if(c==bo)nxt();
			else return null;
			skipWhiteSpace();
			HashMap<Object,Object> r=new HashMap<Object,Object>();
			Object k,v;Boolean t=new Boolean(true);
			while(c!='\0'&&c!=bc)
			{v=t;
				k=parseItem();//if(c=='"'||c=='\''||c=='`')k=extractStringLiteral();else k=extractIdentifier();
				skipWhiteSpace();
				if(c==':'||c=='='){//||Character.isWhitespace(c)
					nxt();
					v=parseItem();
					skipWhiteSpace();
				}//else if(c==','){nxt();
				if(c!='\0'&&c!=bc){
					if(c!=',')
						System.out.print(//throw new IllegalArgumentException(
								"Object:"+rc()+" expected '"+bc+"' or ','");
					nxt();
					skipWhiteSpace();
				}
				r.put(k,v);
			}
			if(c==bc)
				nxt();
			skipRWS();
			return r;}

		public void skipWhiteSpace(){
			boolean b=false;do{
				while(b=Character.isWhitespace(c))nxt();
				b=b||(c=='/'&&skipComments());}while(b);}

		public boolean skipComments(){
			char c2=peek();if(c2=='/'||c2=='*'){nxt();nxt();
				StringBuilder b=new StringBuilder();if(c2=='/')
				{while(c!='\0'&&c!='\n'&&c!='\r')bNxt();
					if(c=='\n'||c=='\r'){nxt();if(c=='\n'||c=='\r')nxt();}
				}else
				{while(c!='\0'&&c2!='/'){bNxt();if(c=='*')c2=peek();}
					if(c=='*'&&c2=='/'){b.deleteCharAt(b.length()-1);nxt();nxt();}
				}comments=b.toString();return true;}return false;}

		/**read a char from the rdr*/
		char read(){
			int h=-1;try{h=rdr.read();}
			catch(Exception ex){TL.tl().error(ex, "TL.Json.Prsr.read");}
			char c= h==-1?'\0':(char)h;
			return c;}

		public char peek(){
			char c='\0';
			int n=lookahead.length();
			if(n<1){
				c=read();
				lookahead.append(c);}
			else c=lookahead.charAt(0);
			return c;}

		public int _row,_col;String rc(){return "("+_row+','+_col+')';}
		public void nlRC(){_col=1;_row++;}public void incCol(){_col++;}
		//boolean eof,mode2=false;
		public char setEof(){return c='\0';}

		/**update the instance-vars (if needed):c,row,col,eof*/
		public char nxt(char h){
			if(h=='\0'||h==-1||c=='\0')return setEof();
				//if(c=='\0')return setEof();//c='\0';
			else c=h;
			if(c=='\n')
				nlRC();
			else incCol();
			return c;}

		/**put into the buffer the current c , and then call nxt()*/
		public char bNxt(){buff();return nxt();}

		/**read from the reader a char and store the read char into member-variable c, @returns member-variable c*/
		public char nxt(){
			char h='\0';
			if(c=='\0')return setEof();//=h;
			if(lookahead.length()>0){
				h=lookahead.charAt(0);
				lookahead.deleteCharAt(0);
			}else h=read();
			c=nxt(h);
			return c;}

		/**this method works differently than next(), in particular how char c is read and buffered*/
		public String next(int n)
		{String old=consume(),retVal=null;while(n-->0)buff(nxt());retVal=consume();buff.append(old);return retVal;}

		public char buff(){return buff(c);}
		char buff(char p){buff.append(p);return p;}

		/**empty the member-variable buff , @returns what was stored in buff*/
		public String consume(){String s=buff.toString();buff.replace(0, buff.length(), "");return s;}

		public boolean lookahead(String p,int offset){
			int i=0,pn=p.length()-offset,ln=lookahead.length();
			boolean b=false;char c=0,h=0;if(pn>0)
				do{h=p.charAt(i+offset);
					if(i<ln)
						c=lookahead.charAt(i);
					else{
						c=read();
						lookahead.append(c);
					}
				}while( (b=(c==h ||
						            Character.toUpperCase(c)==
								            Character.toUpperCase(h))
				)&& (++i)<pn );
			return b;}

		public boolean lookahead(String p){return lookahead(p,0);}

	}//Prsr
}//class Json


//////////////////////////////////////////////////////////////////////

public static class Req implements HttpServletRequest {
	public Ssn ssn;//=new Ssn();
	String contentType="text/json"
			,protocolVersion=""
			,uri="",bodyData//,data
			,queryString=""
					 ;//,method="POST";//
	PC pc;Req(PC p){pc=p;}Req(PC p,String data){pc=p;init(data);}
	InputStream inps;// from Http.Response.data
	BufferedReader bufr;
	long contentLength;// from Http.Response
	boolean chunkedTransfer;// from Http.Response
	boolean keepAlive;      // from Http.Response
	List<String> cookieHeaders;// from Http.Response
	String methd;Method method= Method.GET;// from Http.Response.requestMethod
	/**
	 * HTTP Request methods, with the ability to decode a <code>String</code> back
	 * to its enum value.
	 */
	public enum Method {
		GET,
		PUT(true),
		POST(true),
		DELETE,
		HEAD,
		OPTIONS,
		TRACE,
		CONNECT,
		PATCH,
		PROPFIND,
		PROPPATCH,
		MKCOL,
		MOVE,
		COPY,
		LOCK,
		UNLOCK;Method(){this(false);}
		boolean bodyData;Method(boolean bData){bodyData=bData;}
		public static Method lookup(String method,Method defVal) {
			if (method != null)try {
				defVal= valueOf(method);
			} catch (IllegalArgumentException e) {}
			return defVal;
		}
	}
	GzipUsage gzipUsage = GzipUsage.DEFAULT;// from Http.Response
	static enum GzipUsage {DEFAULT,ALWAYS,NEVER;}// from Http.Response


	//public void addCookieHeader(String cookie) {cookieHeaders.add(cookie);}

	public Req init( String[] data) {
		try {method= Method.valueOf( methd=data[0] );}catch ( Exception ex){method=null;}
		uri=data[1];
		return init(bodyData=data[2]);}

	Req init( String data) {contentLength=(bodyData=data).length();
		inps = new ByteArrayInputStream(bodyData.getBytes());bufr=null;
		return this;}

	Req init( String mimeType, InputStream data, long totalBytes) {
		contentType= mimeType;
		if (data == null) {
			inps = new ByteArrayInputStream(new byte[0]);bufr=null;
			contentLength = 0L;
		} else {
			inps = data;bufr=null;
			contentLength = totalBytes;
		}
		chunkedTransfer = contentLength < 0;
		keepAlive = true;
		cookieHeaders = new ArrayList(10);
		return this;}

	public void setUseGzip(boolean useGzip) {gzipUsage = useGzip ? GzipUsage.ALWAYS : GzipUsage.NEVER;}
	// If a Gzip usage has been enforced, use it.
	// Else decide whether or not to use Gzip.
	public boolean useGzipWhenAccepted() {
		if (gzipUsage == GzipUsage.DEFAULT)
			return contentType != null && (contentType.toLowerCase().contains("text/") || contentType.toLowerCase().contains("/json"));
		else
			return gzipUsage == GzipUsage.ALWAYS;
	}

	public void close() throws IOException {
		if (inps!= null)
			inps.close();
	}

	public void closeConnection(boolean close) {
		if (close)
			headers.put("connection", "close");
		else
			headers.remove("connection");
	}

	public boolean isCloseConnection() {
		return "close".equals(getHeader("connection"));
	}
	public void setKeepAlive(boolean useKeepAlive) {this.keepAlive = useKeepAlive;}

	String findSessionCookie(){for(String s:cookieHeaders)if(s!=null&&s.indexOf( "session" )!=-1)return s;return null;}

	/**
	 * Decodes the sent headers and loads the data into Key/value pairs
	 */
	void initFromInputStream()throws Exception{
		readHeadersFromInputStream();
		bodyData=readBodyData();//if(method.bodyData)
		decodeParms( bodyData );}

	void readHeadersFromInputStream()throws Exception {//BufferedReader in	, Map<String, String> pre//, Map<String, List<String>> parms, Map<String, String> headers
		try {BufferedReader in=getReader();
			// Read the request line
			String line = in.readLine();
			if (line == null)
				return;

			StringTokenizer st = new StringTokenizer(line);
			if (!st.hasMoreTokens())
				throw new Exception( "BAD REQUEST: Syntax error. ");//Rsp.Status.BAD_REQUEST,

			method= Method.lookup( st.nextToken(), Method.GET );//method=;//pre.put("method",);

			if (!st.hasMoreTokens())
				throw new Exception("BAD REQUEST: Missing URI. ");//Status.BAD_REQUEST,


			uri = st.nextToken();//String

			// Decode parameters from the URI
			int qmi = uri.indexOf('?');
			if (qmi >= 0) {
				decodeParms(queryString=uri.substring(qmi + 1));
				uri = urlDecode(uri.substring(0, qmi));
			} else
				uri = urlDecode(uri);

			// If there's another token, its protocol version,
			// followed by HTTP headers.
			// NOTE: this now forces header names lower case since they are
			// case insensitive and vary by client.
			if (st.hasMoreTokens()) {
				protocolVersion = st.nextToken();
			} else {
				protocolVersion = "HTTP/1.1";
				TL.tl().log("no protocol version specified, strange. Assuming HTTP/1.1.");//NanoHTTPD.LOG.log(Level.FINE,
			}
			line = in.readLine();
			while (line != null && !line.trim().isEmpty()) {
				int p = line.indexOf(':');
				if (p >= 0) {
					String hname=line.substring(0, p).trim().toLowerCase(Locale.US)
							,hval=line.substring(p + 1).trim();
					if("cookie".equals(hval))
						cookieHeaders.add( hval );else
						headers.put(hname, hval);
				}
				line = in.readLine();
			}

			//uri;//pre.put("uri",);
		} catch (IOException ioe) {
			throw new Exception( "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage(), ioe);//Status.INTERNAL_ERROR,
		}
	}

	String readBodyData(){
		StringBuilder b=new StringBuilder(  );
		try {
			BufferedReader in = getReader();
			String line = in.readLine();
			if(line!=null){b.append( line );
				in.readLine();}
			while (line!=null ) {
				b.append( '\n' ).append( line );
				in.readLine();}
		}catch ( Exception ex ){}
		return b.toString();}

	void decodeParms(String parms){
		StringTokenizer st = new StringTokenizer(parms, "&");
		while (st.hasMoreTokens()) {
			String e = st.nextToken();
			int sep = e.indexOf('=');
			String key = null;
			String value = null;

			if (sep >= 0) {
				key = urlDecode(e.substring(0, sep)).trim();
				value = urlDecode(e.substring(sep + 1));
			} else {
				key = urlDecode(e).trim();
				value = "";
			}

			String[] values=null,a = prms.get(key);
			int n=a==null?0:a.length;
			values=new String[n+1];
			if(n>0)
				System.arraycopy( a,0,values,0,n );
			values[n]=value;
			prms.put(key, values);
		}
	}


	/**
	 * Find the byte positions where multipart boundaries start. This reads a
	 * large block at a time and uses a temporary buffer to optimize (memory
	 * mapped) file access.
	 */
	private int[] getBoundaryPositions(ByteBuffer b, byte[] boundary) {
		int[] res = new int[0];
		if (b.remaining() < boundary.length) {
			return res;
		}

		int search_window_pos = 0;
		byte[] search_window = new byte[4 * 1024 + boundary.length];

		int first_fill = (b.remaining() < search_window.length) ? b.remaining() : search_window.length;
		b.get(search_window, 0, first_fill);
		int new_bytes = first_fill - boundary.length;

		do {
			// Search the search_window
			for (int j = 0; j < new_bytes; j++) {
				for (int i = 0; i < boundary.length; i++) {
					if (search_window[j + i] != boundary[i])
						break;
					if (i == boundary.length - 1) {
						// Match found, add it to results
						int[] new_res = new int[res.length + 1];
						System.arraycopy(res, 0, new_res, 0, res.length);
						new_res[res.length] = search_window_pos + j;
						res = new_res;
					}
				}
			}
			search_window_pos += new_bytes;

			// Copy the end of the buffer to the start
			System.arraycopy(search_window, search_window.length - boundary.length, search_window, 0, boundary.length);

			// Refill search_window
			new_bytes = search_window.length - boundary.length;
			new_bytes = (b.remaining() < new_bytes) ? b.remaining() : new_bytes;
			b.get(search_window, boundary.length, new_bytes);
		} while (new_bytes > 0);
		return res;
	}


	/**
	 * Decode percent encoded <code>String</code> values.
	 *
	 * @param str the percent encoded <code>String</code>
	 * @return expanded form of the input, for example "foo%20bar" becomes
	 * "foo bar"
	 */
	public static String urlDecode( String str ) {
		String decoded = null;
		try {
			decoded = URLDecoder.decode( str, "UTF8" );
		} catch ( UnsupportedEncodingException ex ) {
			//NanoHTTPD.LOG.log( Level.WARNING, "Encoding not supported, ignored", ignored );
			TL.tl().error(ex,"Encoding not supported, ignored");
		}
		return decoded;
/*
	/**
	 * Decodes the Multipart Body data and put it into Key/Value pairs.
	 * /
		private void decodeMultipartFormData(ContentType contentType, ByteBuffer fbuf, Map<String, List<String>> parms, Map<String, String> files) throws NanoHTTPD.ResponseException {
			int pcount = 0;
			try {
				int[] boundaryIdxs = getBoundaryPositions(fbuf, contentType.getBoundary().getBytes());
				if (boundaryIdxs.length < 2) {
					throw new NanoHTTPD.ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but contains less than two boundary strings.");
				}
				byte[] partHeaderBuff = new byte[MAX_HEADER_SIZE];
				for (int boundaryIdx = 0; boundaryIdx < boundaryIdxs.length - 1; boundaryIdx++) {
					fbuf.position(boundaryIdxs[boundaryIdx]);
					int len = (fbuf.remaining() < MAX_HEADER_SIZE) ? fbuf.remaining() : MAX_HEADER_SIZE;
					fbuf.get(partHeaderBuff, 0, len);
					BufferedReader in =
							new BufferedReader(new InputStreamReader(new ByteArrayInputStream(partHeaderBuff, 0, len), Charset.forName(contentType.getEncoding())), len);
					int headerLines = 0;
					// First line is boundary string
					String mpline = in.readLine();
					headerLines++;
					if (mpline == null || !mpline.contains(contentType.getBoundary())) {
						throw new NanoHTTPD.ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but chunk does not start with boundary.");
					}
					String partName = null, fileName = null, partContentType = null;
					// Parse the reset of the header lines
					mpline = in.readLine();
					headerLines++;
					while (mpline != null && mpline.trim().length() > 0) {
						Matcher matcher = NanoHTTPD.CONTENT_DISPOSITION_PATTERN.matcher(mpline);
						if (matcher.matches()) {
							String attributeString = matcher.group(2);
							matcher = NanoHTTPD.CONTENT_DISPOSITION_ATTRIBUTE_PATTERN.matcher(attributeString);
							while (matcher.find()) {
								String key = matcher.group(1);
								if ("name".equalsIgnoreCase(key)) {
									partName = matcher.group(2);
								} else if ("filename".equalsIgnoreCase(key)) {
									fileName = matcher.group(2);
									// add these two line to support multiple
									// files uploaded using the same field Id
									if (!fileName.isEmpty()) {
										if (pcount > 0)
											partName = partName + String.valueOf(pcount++);
										else
											pcount++;
									}
								}
							}
						}
						matcher = NanoHTTPD.CONTENT_TYPE_PATTERN.matcher(mpline);
						if (matcher.matches()) {
							partContentType = matcher.group(2).trim();
						}
						mpline = in.readLine();
						headerLines++;
					}
					int partHeaderLength = 0;
					while (headerLines-- > 0) {
						partHeaderLength = scipOverNewLine(partHeaderBuff, partHeaderLength);
					}
					// Read the part data
					if (partHeaderLength >= len - 4) {
						throw new NanoHTTPD.ResponseException(Status.INTERNAL_ERROR, "Multipart header size exceeds MAX_HEADER_SIZE.");
					}
					int partDataStart = boundaryIdxs[boundaryIdx] + partHeaderLength;
					int partDataEnd = boundaryIdxs[boundaryIdx + 1] - 4;
					fbuf.position(partDataStart);
					List<String> values = parms.get(partName);
					if (values == null) {
						values = new ArrayList<String>();
						parms.put(partName, values);
					}
					if (partContentType == null) {
						// Read the part into a string
						byte[] data_bytes = new byte[partDataEnd - partDataStart];
						fbuf.get(data_bytes);
						values.add(new String(data_bytes, contentType.getEncoding()));
					} else {
						// Read it into a file
						String path = saveTmpFile(fbuf, partDataStart, partDataEnd - partDataStart, fileName);
						if (!files.containsKey(partName)) {
							files.put(partName, path);
						} else {
							int count = 2;
							while (files.containsKey(partName + count)) {
								count++;
							}
							files.put(partName + count, path);
						}
						values.add(fileName);
					}
				}
			} catch (NanoHTTPD.ResponseException re) {
				throw re;
			} catch (Exception e) {
				throw new NanoHTTPD.ResponseException(Status.INTERNAL_ERROR, e.toString());
			}
		}
	public void parseBody(Map<String, String> files) throws IOException, NanoHTTPD.ResponseException {
		RandomAccessFile randomAccessFile = null;
		try {
			long size = getBodySize();
			ByteArrayOutputStream baos = null;
			DataOutput requestDataOutput = null;
			// Store the request in memory or a file, depending on size
			if (size < MEMORY_STORE_LIMIT) {
				baos = new ByteArrayOutputStream();
				requestDataOutput = new DataOutputStream(baos);
			} else {
				randomAccessFile = getTmpBucket();
				requestDataOutput = randomAccessFile;
			}
			// Read all the body and write it to request_data_output
			byte[] buf = new byte[REQUEST_BUFFER_LEN];
			while (this.rlen >= 0 && size > 0) {
				this.rlen = this.inputStream.read(buf, 0, (int) Math.min(size, REQUEST_BUFFER_LEN));
				size -= this.rlen;
				if (this.rlen > 0) {
					requestDataOutput.write(buf, 0, this.rlen);
				}
			}
			ByteBuffer fbuf = null;
			if (baos != null) {
				fbuf = ByteBuffer.wrap(baos.toByteArray(), 0, baos.size());
			} else {
				fbuf = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, randomAccessFile.length());
				randomAccessFile.seek(0);
			}
			// If the method is POST, there may be parameters
			// in data section, too, read it:
			if (Method.POST.equals(this.method)) {
				ContentType contentType = new ContentType(this.headers.get("content-type"));
				if (contentType.isMultipart()) {
					String boundary = contentType.getBoundary();
					if (boundary == null) {
						throw new NanoHTTPD.ResponseException(Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but boundary missing. Usage: GET /example/file.html");
					}
					decodeMultipartFormData(contentType, fbuf, this.parms, files);
				} else {
					byte[] postBytes = new byte[fbuf.remaining()];
					fbuf.get(postBytes);
					String postLine = new String(postBytes, contentType.getEncoding()).trim();
					// Handle application/x-www-form-urlencoded
					if ("application/x-www-form-urlencoded".equalsIgnoreCase(contentType.getContentType())) {
						decodeParms(postLine, this.parms);
					} else if (postLine.length() != 0) {
						// Special case for raw POST data => create a
						// special files entry "postData" with raw content
						// data
						files.put(POST_DATA, postLine);
					}
				}
			} else if (Method.PUT.equals(this.method)) {
				files.put("content", saveTmpFile(fbuf, 0, fbuf.limit(), null));
			}
		} finally {
			NanoHTTPD.safeClose(randomAccessFile);
		}
	}*/}



	//"{op:'query',sql:'select d,count(*),min(no),max(no) from d group by y,m,w'}"
	HashMap<String,Object> attribs=new HashMap<String,Object>();
	HashMap<String,String> headers=new HashMap<String,String>();
	HashMap<String,String[]>prms=new HashMap<String,String[]>();
	@Override public AsyncContext getAsyncContext() {return null;}
	@Override public Object getAttribute(String p) {return attribs.get(p);}
	@Override public Enumeration<String> getAttributeNames() {return new Enumeration<String>() {
		Iterator<String>i=attribs.keySet().iterator();
		@Override	public boolean hasMoreElements() {	return i.hasNext();}
		@Override	public String nextElement() {return i.next();}};}
	@Override public String getCharacterEncoding() {return "utf8";}
	@Override public int getContentLength() {return bodyData==null?(int)contentLength:bodyData.length();}
	@Override public long getContentLengthLong() {return bodyData==null?0:bodyData.length();}
	@Override public String getContentType() {p("Req.getContentType:",contentType);return contentType;}
	@Override public String getContextPath() {return uri;}
	@Override public DispatcherType getDispatcherType() {return null;}
	@Override public ServletInputStream getInputStream() throws IOException{
		return new ServletInputStream() {int i=0;
			@Override public int read() throws IOException{return bodyData.charAt(i++);}
			@Override public void setReadListener(ReadListener p){}
			@Override public boolean isReady() {return true;}
			@Override public boolean isFinished() {return i>=bodyData.length();}};}//new java.io.ByteArrayInputStream(data.getBytes());}//(new java.io.StringReader(data));//StringBufferInputStream(data);
	@Override public String getLocalAddr() {return null;}
	@Override public String getLocalName() {return null;}
	@Override public int getLocalPort() {return 0;}
	@Override public Locale getLocale() {return null;}
	@Override public Enumeration<Locale> getLocales() {return null;}
	@Override public String getParameter(String p){String[]a= prms.get(p);return a!=null&&a.length>0?a[0]:null;}
	@Override public Map<String, String[]> getParameterMap() {return prms;}
	@Override public Enumeration<String> getParameterNames() {return new Enumeration<String>() {
		Iterator<String>i=prms.keySet().iterator();
		@Override	public boolean hasMoreElements() {	return i.hasNext();}
		@Override	public String nextElement() {return i.next();}};}
	@Override public String[] getParameterValues(String p) {return prms.get(p);}
	@Override public String getProtocol() {return protocolVersion;}
	@Override public BufferedReader getReader() throws IOException {
		return bufr!=null?bufr:(bufr=new BufferedReader( new InputStreamReader(inps )));}////new BufferedReader(new java.io.CharArrayReader(data.toCharArray()));
	@Override public String getRealPath(String p) {return null;}
	@Override public String getRemoteAddr() {return "127.0.0.1";}
	@Override public String getRemoteHost() {return null;}
	@Override public int getRemotePort() {return 0;}
	@Override public RequestDispatcher getRequestDispatcher(String p) {return null;}
	@Override public String getScheme() {return null;}
	@Override public String getServerName() {return null;}
	@Override public int getServerPort() {return 0;}
	@Override public ServletContext getServletContext() {return null;}
	@Override public boolean isAsyncStarted() {return false;}
	@Override public boolean isAsyncSupported() {return false;}
	@Override public boolean isSecure() {return false;}
	@Override public void removeAttribute(String p) {attribs.remove(p);}
	@Override public void setAttribute(String p, Object p2) {attribs.put(p, p2);}
	@Override public void setCharacterEncoding(String p) throws UnsupportedEncodingException {}
	@Override public AsyncContext startAsync() throws IllegalStateException {return null;}
	@Override public AsyncContext startAsync(ServletRequest p, ServletResponse p2) throws IllegalStateException {return null;}
	@Override public boolean authenticate(HttpServletResponse p) throws IOException, ServletException {return false;}
	@Override public String changeSessionId() {return null;}
	@Override public String getAuthType() {return null;}
	@Override public Cookie[] getCookies() {return null;}
	@Override public long getDateHeader(String p) {return 0;}
	@Override public String getHeader(String p) {return headers.get(p);}
	@Override public Enumeration<String> getHeaderNames() {return new Enumeration<String>() {
		Iterator<String>i=headers.keySet().iterator();
		@Override	public boolean hasMoreElements() {	return i.hasNext();}
		@Override	public String nextElement() {return i.next();}};}
	@Override public Enumeration<String> getHeaders(String p) {return null;}
	@Override public int getIntHeader(String p) {return 0;}
	@Override public String getMethod() {return method==null?methd:method.name();}
	@Override public Part getPart(String p) throws IOException, ServletException {return null;}
	@Override public Collection<Part> getParts() throws IOException, ServletException {return null;}
	@Override public String getPathInfo() {return null;}
	@Override public String getPathTranslated() {return null;}
	@Override public String getQueryString() {return "/adoqs/xhr.jsp";}
	@Override public String getRemoteUser() {return null;}
	@Override public String getRequestURI() {return uri;}
	@Override public StringBuffer getRequestURL() {return new StringBuffer( uri);}
	@Override public String getRequestedSessionId() {return null;}
	@Override public String getServletPath() {return null;}
	@Override public HttpSession getSession() {return ssn;}
	@Override public HttpSession getSession(boolean p) {return ssn;}
	@Override public java.security.Principal getUserPrincipal() {return null;}
	@Override public boolean isRequestedSessionIdFromCookie() {return false;}
	@Override public boolean isRequestedSessionIdFromURL() {return false;}
	@Override public boolean isRequestedSessionIdFromUrl() {return false;}
	@Override public boolean isRequestedSessionIdValid() {return false;}
	@Override public boolean isUserInRole(String p) {return false;}
	@Override public void login(String p, String p2) throws ServletException {}
	@Override public void logout() throws ServletException {}
	@Override public <T extends HttpUpgradeHandler> T upgrade(Class<T>
			                                                          p) throws IOException, ServletException {return null;}

}//class Req

//////////////////////////////////////////////////////////////////////

public static class Rsp implements HttpServletResponse{
	String contentType="";
	Sos sos;
	static final String Name= Dbg.Name+".Rsp";
	PC pc;
	Rsp(PC p){pc=p;}
	HashMap<String,String> headers=new HashMap<String,String>();

	boolean chunkedTransfer;//from Http.Response
	Status status;//from Http.Response

	/**
	 * Some HTTP response status codes
	 */
	public enum Status{// implements IStatus
		SWITCH_PROTOCOL(101, "Switching Protocols"),

		OK(200, "OK"),
		CREATED(201, "Created"),
		ACCEPTED(202, "Accepted"),
		NO_CONTENT(204, "No Content"),
		PARTIAL_CONTENT(206, "Partial Content"),
		MULTI_STATUS(207, "Multi-Status"),

		REDIRECT(301, "Moved Permanently"),
		/**
		 * Many user agents mishandle 302 in ways that violate the RFC1945 spec
		 * (i.e., redirect a POST to a GET). 303 and 307 were added in RFC2616 to
		 * address this. You should prefer 303 and 307 unless the calling user agent
		 * does not support 303 and 307 functionality
		 */
		@Deprecated
		FOUND(302, "Found"),
		REDIRECT_SEE_OTHER(303, "See Other"),
		NOT_MODIFIED(304, "Not Modified"),
		TEMPORARY_REDIRECT(307, "Temporary Redirect"),

		BAD_REQUEST(400, "Bad Request"),
		UNAUTHORIZED(401, "Unauthorized"),
		FORBIDDEN(403, "Forbidden"),
		NOT_FOUND(404, "Not Found"),
		METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
		NOT_ACCEPTABLE(406, "Not Acceptable"),
		REQUEST_TIMEOUT(408, "Request Timeout"),
		CONFLICT(409, "Conflict"),
		GONE(410, "Gone"),
		LENGTH_REQUIRED(411, "Length Required"),
		PRECONDITION_FAILED(412, "Precondition Failed"),
		PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
		UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
		RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),
		EXPECTATION_FAILED(417, "Expectation Failed"),
		TOO_MANY_REQUESTS(429, "Too Many Requests"),

		INTERNAL_ERROR(500, "Internal Server Error"),
		NOT_IMPLEMENTED(501, "Not Implemented"),
		SERVICE_UNAVAILABLE(503, "Service Unavailable"),
		UNSUPPORTED_HTTP_VERSION(505, "HTTP Version Not Supported");

		private final int requestStatus;

		private final String description;

		Status(int requestStatus, String description) {
			this.requestStatus = requestStatus;
			this.description = description;
		}

		public static Status lookup(int requestStatus) {
			for (Status status : Status.values()) {
				if (status.requestStatus == requestStatus) {
					return status;
				}
			}
			return null;
		}

		public String getDescription() {        return "" + this.requestStatus + " " + this.description;}

	}
	JspWrtr jspWrtr=new JspWrtr();
	PrintWriter out=new PrintWriter(jspWrtr);//System.out//strW);StringWriter strW=new StringWriter(); //TODO: with the server, must not initialize out, instead should remove the member-variable
	@Override public void flushBuffer() throws IOException {if(out!=null)out.flush();if(sos!=null)sos.flush();}
	@Override public int getBufferSize() {p(Name,".getBufferSize:0");return 0;}
	@Override public String getCharacterEncoding() {p(Name,".getCharacterEcoding");return null;}
	@Override public String getContentType() {p(Name,".getContentType:",contentType);return contentType;}
	@Override public Locale getLocale() {p(Name,".getLocale");return null;}
	@Override public ServletOutputStream getOutputStream() throws IOException {p(Name,".getOutputStream");return sos;}
	@Override public PrintWriter getWriter() throws IOException {p(Name,".getWriter");return out;}
	@Override public boolean isCommitted() {p(Name,".isCommited");return false;}
	@Override public void reset() {p(Name,".reset");}
	@Override public void resetBuffer() {p(Name,".resetBuffer");}
	@Override public void setBufferSize(int p) {p(Name,".setBufferSize:",p);}
	@Override public void setCharacterEncoding(String p) {p(Name,".setCharacterEncoding:",p);}
	@Override public void setContentLength(int p) {p(Name,".setContentLength:",p);}
	@Override public void setContentLengthLong(long p) {p(Name,".setContentLengthLong:",p);}
	@Override public void setContentType(String p) {p(Name,".setContentType:",p);contentType=p;}
	@Override public void setLocale(Locale p) {p(Name,".setLocale:",p);}
	@Override public void addCookie(Cookie p) {p(Name,".addCookie:",p);}
	@Override public void addDateHeader(String p, long p2) {p(Name,".addDateHeader:",p,",",p2);}
	@Override public void addHeader(String p, String p2) {p(Name,".addHeader:",p,",",p2);}
	@Override public void addIntHeader(String p, int p2) {p(Name,".addIntHeader:",p,",",p2);}
	@Override public boolean containsHeader(String p) {p(Name,".containsHeader:",p);return false;}
	@Override public String encodeRedirectURL(String p) {p(Name,".ecodeRedirectURL:",p);return null;}
	@Override public String encodeRedirectUrl(String p) {p(Name,".encodeRedirectUrl:",p);return null;}
	@Override public String encodeURL(String p) {p(Name,".encodeURL:",p);return null;}
	@Override public String encodeUrl(String p) {p(Name,".encodeUrl:",p);return null;}
	@Override public String getHeader(String p) {p(Name,".getHeader:",p);return null;}
	@Override public Collection<String> getHeaderNames() {p(Name,".getHeaderNames");return null;}
	@Override public Collection<String> getHeaders(String p) {p(Name,".getHeaders:",p);return null;}
	@Override public int getStatus() {p(Name,".getStatus");return 0;}
	@Override public void sendError(int p) throws IOException {p(Name,".sendError:",p);}
	@Override public void sendError(int p, String p2) throws IOException {p(Name,".sendError:",p,",",p2);}
	@Override public void sendRedirect(String p) throws IOException {p(Name,".sendRedirect:",p);}
	@Override public void setDateHeader(String p, long p2) {p(Name,".setDateHeader:",p,",",p2);}
	@Override public void setHeader(String p, String p2) {p(Name,".setHeader:",p,",",p2);}
	@Override public void setIntHeader(String p, int p2) {p(Name,".setIntHeader:",p,",",p2);}
	@Override public void setStatus(int p) {p(Name,".setStatus:",p);}
	@Override public void setStatus(int p, String p2) {p(Name,".setStatus:",p,",",p2);}

	public static class Sos extends ServletOutputStream{
		OutputStream o;StringBuilder sb=new StringBuilder();
		Sos(OutputStream p){p("\n------------------------------\nSos.<init>:",o=p);}
		@Override public boolean isReady() {return true;}
		@Override public void setWriteListener(WriteListener p) {}//super.setWriteListener(p);}
		@Override public void write(int p) throws IOException {sb.append((char)p);if(o!=null)o.write( p );}//p("Sos.write(int:",p,"):",(char)p);}//o.write(p);
		@Override public void flush() throws IOException {p("Dbg.Sos.flush:",sb.toString());sb.setLength(0);if(o!=null)o.flush();}//super.flush();}//o.flush();
		@Override public void close() throws IOException {p("Dbg.Sos.close:",sb.toString());sb.setLength(0);if(o!=null)o.close();}//super.close();}//o.close();
		@Override public void write(byte[] p) throws IOException {sb.append(new String(p));if(o!=null)o.write(p);}//p("Sos.write(byte[]):",new String(p));super.write(p);}//o.write(p);}
		@Override public void write(byte[] a, int b, int c) throws IOException {sb.append(new String(a,b,c));if(o!=null)o.write(a,b,c);}//p("Sos.write(byte[]:",a,",int:",b,",int:",c,"):",new String(a, b, c));super.write(a, b, c);}//o.write(a, b, c);}
		public void p(Object...p){for(Object o:p)try{print(String.valueOf( o ));}catch ( Exception ex ){}}
		@Override public void print(String p) throws IOException {sb.append(p);if(o!=null)o.write(p.getBytes());}
		@Override public void print(int p) throws IOException {p(p);}
		@Override public void print(boolean p) throws IOException {p(p);}
		@Override public void print(char p) throws IOException {p(p);}
		@Override public void print(double p) throws IOException {p(p);}
		@Override public void print(float p) throws IOException {p(p);}
		@Override public void print(long p) throws IOException {p(p);}
		@Override public void println(int p) throws IOException {p(p,'\n');}
		@Override public void println(boolean p) throws IOException {p(p,'\n');}
		@Override public void println() throws IOException {p('\n');}
		@Override public void println(char p) throws IOException {p(p,'\n');}
		@Override public void println(double p) throws IOException {p(p,'\n');}
		@Override public void println(float p) throws IOException {p(p,'\n');}
		@Override public void println(long p) throws IOException {p(p,'\n');}
		@Override public void println(String p) throws IOException {p(p,'\n');}
	}//class Sos

	public static class JspWrtr extends javax.servlet.jsp.JspWriter{

		@Override public void clear() throws IOException { clearBuffer();}
		@Override public void clearBuffer() throws IOException {sb.replace( 0,sb.length(),"" ); }
		//@Override public void close() throws IOException { }
		@Override public void newLine() throws IOException { println();}
		//@Override public void flush() throws IOException { }
		@Override public int getRemaining() {return sb.length(); }

		//@Override public void write( int i ) throws IOException { }
		//@Override public void write(char[]a, int i,int n ) throws IOException { }
		//@Override public void write( String i ) throws IOException { }

		@Override public void print( int i ) throws IOException { sb.append( i );}
		@Override public void print( boolean i ) throws IOException { sb.append( i );}
		@Override public void print( char i ) throws IOException { sb.append( i );}
		@Override public void print( char[] i ) throws IOException { sb.append( i );}
		@Override public void print( long i ) throws IOException { sb.append( i );}
		@Override public void print( float i ) throws IOException { sb.append( i );}
		@Override public void print( double i ) throws IOException { sb.append( i );}
		@Override public void print( String i ) throws IOException { sb.append( i );}
		@Override public void print( Object i ) throws IOException { sb.append( i );}

		@Override public void println( int i ) throws IOException { sb.append( i ).append( '\n' );}
		@Override public void println( boolean i ) throws IOException { sb.append( i ).append( '\n' );}
		@Override public void println( char i ) throws IOException { sb.append( i ).append( '\n' );}
		@Override public void println( char[] i ) throws IOException { sb.append( i ).append( '\n' );}
		@Override public void println( long i ) throws IOException { sb.append( i ).append( '\n' );}
		@Override public void println( float i ) throws IOException { sb.append( i ).append( '\n' );}
		@Override public void println( double i ) throws IOException { sb.append( i ).append( '\n' );}
		@Override public void println( String i ) throws IOException { sb.append( i ).append( '\n' );}
		@Override public void println( Object i ) throws IOException { sb.append( i ).append( '\n' );}
		@Override public void println( ) throws IOException { sb.append( '\n' );}


		//public static class SrvltWrtr extends java.io.Writer{
		//SrvltWrtr
		JspWrtr(){super(10,false);
			p("SrvltWrtr.<init>");}
		StringBuilder sb=new StringBuilder();
		@Override public void flush() throws IOException {p("SrvltWrtr.flush:",sb.toString());sb.setLength(0);}
		@Override public void close() throws IOException {p("SrvltWrtr.close:",sb.toString());sb.setLength(0);}
		//@Override public String toString() {String s=sb.toString();p("SrvltWrtr.toString:",s);return s;}
		@Override public void write(char[] cbuf, int off, int len) throws IOException {sb.append(cbuf, off, len);}
		@Override public void write(char[] cbuf) throws IOException {sb.append(cbuf);}
		@Override public void write(String p) throws IOException {sb.append(p);}
		@Override public void write(String p, int off, int len) throws IOException {sb.append(p, off, len);}
		//p("SrvltWrtr.write(char[]",cbuf,",off=",off,",len=",len,"):",String.valueOf(cbuf,off,len));
		@Override public void write(int p){sb.append((char)p);}
		@Override public Writer append(CharSequence p) throws IOException {sb.append(p);return this;}
		@Override public Writer append(CharSequence p, int off, int len) throws IOException {sb.append(p, off, len);return this;}
		@Override public Writer append(char p) throws IOException {sb.append(p);return this;}
		//@Override public Writer append(String p)  {sb.append(p);super.;return this;}
		//@Override public Writer append(String p, int off, int len) throws IOException {sb.append(p, off, len);return this;}
		//}//public static class SrvltWrtr extends java.io.Writer
	}//class JspWrtr , writer

}//class Rsp

//////////////////////////////////////////////////////////////////////

public static class PC extends javax.servlet.jsp.PageContext{
	public Req q=new Req(this);public Rsp p=new Rsp(this);
	public SrvltContxt a=null;Socket socket;


	@Override public void forward(String arg0) throws ServletException, IOException {}
	@Override public Exception getException() {return null;}
	@Override public Object getPage() {return null;}
	@Override public ServletRequest getRequest(){return q;}
	@Override public ServletResponse getResponse(){return p;}
	@Override public ServletConfig getServletConfig(){return Srvlt.sttc.getServletConfig();}
	@Override public ServletContext getServletContext(){return Srvlt.sttc.getServletContext();}
	@Override public HttpSession getSession(){return q.ssn;}
	@Override public void handlePageException(Exception arg0) throws ServletException, IOException{}
	@Override public void handlePageException(Throwable arg0) throws ServletException, IOException{}
	@Override public void include(String arg0) throws ServletException, IOException{}
	@Override public void include(String arg0, boolean arg1) throws ServletException, IOException{}
	@Override public void initialize(Servlet arg0, ServletRequest arg1, ServletResponse arg2, String arg3,
	                                 boolean arg4, int arg5,boolean arg6) throws IOException, IllegalStateException, IllegalArgumentException{}
	@Override public void release(){}
	@Override public Object findAttribute(String n){Object o=q.getAttribute(n);
		if(o==null)o=q.ssn.getAttribute(n);if(o==null)o=a.getAttribute(n);return o;}
	@Override public Object getAttribute(String n){return findAttribute(n);}
	@Override public Object getAttribute(String n, int arg1){return null;}
	@Override public Enumeration<String> getAttributeNamesInScope(int arg0){return null;}
	@Override public int getAttributesScope(String arg0) {TL.tl().log(
			"Dbg.PC.getAttributesScope:not implemented:return 0");return 0;}

	@Override public javax.servlet.jsp.el.ExpressionEvaluator getExpressionEvaluator(){return null;}
	@Override public javax.servlet.jsp.JspWriter getOut(){TL.tl().log(
			"Dbg.PC.getOut");return Srvlt.sttc.pc.p.jspWrtr;}
	@Override public javax.servlet.jsp.el.VariableResolver getVariableResolver(){return null;}
	@Override public void removeAttribute(String arg0){TL.tl().log(
			"Dbg.PC.removeAttribute a:not implemented");}
	@Override public void removeAttribute(String arg0, int arg1){TL.tl().log("Dbg.PC.removeAttribute a,b:not implemented:return null");}
	@Override public void setAttribute(String arg0, Object arg1) {TL.tl().log("Dbg.PC.setAttribute a,b:not implemented:return null");}
	@Override public void setAttribute(String arg0, Object arg1, int arg2) {TL.tl().log("Dbg.PC.setAttribute a,b,c:not implemented:return null");}
	@Override public javax.el.ELContext getELContext(){TL.tl().log("Dbg.PC.getELContext:not implemented:return null");return null;}
//public static class ELContext{}
}//class PC

//////////////////////////////////////////////////////////////////////

public static class Srvlt extends GenericServlet{
	static final String Name= Dbg.Name+".Srvlt";
	@Override public void service(ServletRequest q, ServletResponse p)throws ServletException, IOException {
		p(Name,".service:",q,",",p);}

	public PC pc=null;//new PC();
	public static Srvlt sttc=new Srvlt();

	@Override public void log(String message, Throwable t) {log(message);t.printStackTrace();}//super.log(message, t);}
	@Override public void log(String msg) {p("log:",msg);}//super.log(msg);
	@Override public ServletContext getServletContext() {return SrvltContxt.sttc();}//pc.a;super.getServletContext()
	@Override public String getServletName() {return Name;}//super.getServletName();
}//class Srvlt


//////////////////////////////////////////////////////////////////////

public static class Ssn implements HttpSession {
	HashMap<String,Object> attribs=new HashMap<String,Object>();long expir;boolean newlySsn=true;
	public static Map<String,Ssn>sessions=new HashMap<String,Ssn>();

	@Override public Object getAttribute(String p){return attribs.get(p);}
	@Override public Enumeration<String> getAttributeNames() {return new Enumeration<String>() {
		Iterator<String>i=attribs.keySet().iterator();
		@Override	public boolean hasMoreElements() {	return i.hasNext();}
		@Override	public String nextElement() {return i.next();}};}
	@Override public long getCreationTime() {return 0;}
	@Override public String getId() {return null;}
	@Override public long getLastAccessedTime() {return 0;}
	@Override public int getMaxInactiveInterval() {return 0;}
	@Override public ServletContext getServletContext() {return SrvltContxt.sttc();}//
	@Override public HttpSessionContext getSessionContext() {return null;}
	@Override public Object getValue(String p){return null;}
	@Override public String[] getValueNames() {return null;}
	@Override public void invalidate(){}
	@Override public boolean isNew() {return newlySsn;}
	@Override public void putValue(String p, Object p2){}
	@Override public void removeAttribute(String p){}
	@Override public void removeValue(String p){}
	@Override public void setAttribute(String k, Object v){attribs.put(k, v);}
	@Override public void setMaxInactiveInterval(int p){}}

//////////////////////////////////////////////////////////////////////

public static class SrvltContxt implements ServletContext{//static SrvltContxt sttc;
	HashMap<String,Object> attribs=new HashMap<String,Object>();
	@Override public FilterRegistration.Dynamic addFilter( String arg0, String p2){return null;}
	@Override public FilterRegistration.Dynamic addFilter(String arg0, Filter p2){return null;}
	@Override public FilterRegistration.Dynamic addFilter(String arg0, Class<? extends Filter> p2){return null;}
	@Override public void addListener(String p){}
	@Override public <T extends EventListener > void addListener( T p){}
	@Override public void addListener(Class<? extends EventListener> p){}
	@Override public ServletRegistration.Dynamic addServlet(String arg0, String p2){return null;}
	@Override public ServletRegistration.Dynamic addServlet(String arg0, Servlet p2){return null;}
	@Override public ServletRegistration.Dynamic addServlet(String arg0, Class<? extends Servlet> p2){return null;}
	@Override public <T extends Filter> T createFilter(Class<T> p2)throws ServletException{return null;}
	@Override public <T extends EventListener> T createListener(Class<T> p2)throws ServletException{return null;}
	@Override public <T extends Servlet> T createServlet(Class<T> p2)throws ServletException{return null;}
	@Override public void declareRoles(String... p){}
	@Override public Object getAttribute(String p){return attribs.get(p);}
	@Override public Enumeration<String> getAttributeNames(){return new Enumeration<String>() {
		Iterator<String>i=attribs.keySet().iterator();
		@Override	public boolean hasMoreElements() {	return i.hasNext();}
		@Override	public String nextElement() {return i.next();}};}
	@Override public ClassLoader getClassLoader(){return null;}
	@Override public ServletContext getContext(String p){return null;}
	@Override public String getContextPath(){return null;}
	@Override public Set<SessionTrackingMode> getDefaultSessionTrackingModes(){return null;}
	@Override public int getEffectiveMajorVersion(){return 0;}
	@Override public int getEffectiveMinorVersion(){return 0;}
	@Override public Set<SessionTrackingMode> getEffectiveSessionTrackingModes(){return null;}
	@Override public FilterRegistration getFilterRegistration(String p){return null;}
	@Override public Map<String, ? extends FilterRegistration> getFilterRegistrations(){return null;}
	@Override public String getInitParameter(String p){return null;}
	@Override public Enumeration<String> getInitParameterNames(){return null;}
	@Override public javax.servlet.descriptor.JspConfigDescriptor getJspConfigDescriptor(){return null;}
	@Override public int getMajorVersion(){return 0;}
	@Override public int getMinorVersion(){return 0;}
	@Override public String getMimeType(String p){
		final String def="application/octet-stream";
		Map m=(Map)attribs.get( "mapFileExt2MimeType" );
		if(m==null) {m=Util.mapCreate(
				"woff","application/font-woff"
				,"woff2","application/font-woff2"
				,"jar"  ,"application/java-archive"
				,"js"   ,"application/javascript"
				,"json" ,"application/json"
				,"exe"  ,def
				,"pdf"  ,"application/pdf"
				,"7z"   ,"application/x-7z-compressed"
				,"tgz"  ,"application/x-compressed"
				,"gz"   ,"application/x-gzip"
				,"tar"  ,"application/x-tar"
				,"xhtml","application/xhtml+xml"
				,"zip"  ,"application/zip"
				,"mp3"  ,"audio/mpeg"
				,"gif"  ,"image/gif"
				,"jpg"  ,"image/jpeg"
				,"jpeg" ,"image/jpeg"
				,"png"  ,"image/png"
				,"svg"  ,"image/svg+xml"
				,"ico"  ,"image/x-icon"
				,"css"  ,"text/css"
				,"csv"  ,"text/csv"
				,"htm"  ,"text/html; charset=utf-8"
				,"html" ,"text/html; charset=utf-8"
				,"txt"  ,"text/plain"
				,"text" ,"text/plain"
				,"log"  ,"text/plain"
				,"xml"  ,"text/xml" );
		}p=p==null?null:(String)m.get(p);
		return p==null?def:p;}
	@Override public RequestDispatcher getNamedDispatcher(String p){return null;}
	@Override public String getRealPath(String p){return context.getRealPath( TL.tl(),p );}
	@Override public RequestDispatcher getRequestDispatcher(String p){return null;}
	@Override public java.net.URL getResource(String arg0) throws java.net.MalformedURLException {return null;}
	@Override public InputStream getResourceAsStream( String p){return null;}
	@Override public Set<String> getResourcePaths(String p){return null;}
	@Override public String getServerInfo(){return null;}
	@Override public Servlet getServlet(String uri)throws ServletException{return null;}
	@Override public String getServletContextName(){return null;}
	@Override public Enumeration<String> getServletNames(){return null;}
	@Override public ServletRegistration getServletRegistration(String p){return null;}
	@Override public Map<String, ? extends ServletRegistration> getServletRegistrations(){return null;}
	@Override public Enumeration<Servlet> getServlets(){return null;}
	@Override public SessionCookieConfig getSessionCookieConfig(){return null;}
	@Override public String getVirtualServerName(){return null;}
	@Override public void log(String p){p("log:",p);}
	@Override public void log(Exception x, String p){x.printStackTrace();p(p);}
	@Override public void log(String p, Throwable x){log(p);x.printStackTrace();}
	@Override public void removeAttribute(String p){attribs.remove( p );}
	@Override public void setAttribute(String p, Object v){p(Name,".SrvltContxt.setAttribute:",p,",",v);attribs.put(p, v);}
	@Override public boolean setInitParameter(String arg0, String p2){return false;}
	@Override public void setSessionTrackingModes(Set<SessionTrackingMode> p){}

	Thread servrThread=null;

	static SrvltContxt sttc;public static SrvltContxt sttc(){return sttc==null?new SrvltContxt():sttc;}
	private SrvltContxt(){if(sttc==null)sttc=this;}

	void startServer(int port){
		servrThread=new Thread( new Runnable() {public void run(){
			try{ServerSocket servrSocket = new ServerSocket( port );
				while(servrThread!=null){
					Socket socket=servrSocket.accept();
					PC pc=new PC();
					pc.q.inps=socket.getInputStream();
					pc.p.sos=new Rsp.Sos( socket.getOutputStream());
					pc.q.initFromInputStream(  );
					if(pc.q.cookieHeaders==null || pc.q.cookieHeaders.size()==0)
					{	if(pc.q.cookieHeaders==null)
						pc.q.cookieHeaders=new LinkedList<>(  );

					}
					Servlet s=getServlet(pc.q.uri);
					if(s==null)
						s=new FSrvlt();
					s.service( pc.q,pc.p );
				}//while
			}catch(Exception ex )
			{p( "startServer:ex", ex ); }
		}//run
		}//Runnable
		);//Thread
		servrThread.start();
	}//startServer

}//SrvltContxt

}
