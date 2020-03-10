import java.lang.reflect.Field;
import java.util.*;
public class AsicsSrvlt extends M {

//////////////////////////////////////////////////////////////////////

//    public static class AsicSrvlt{//GrjSrvlt

        @HttpMethod(usrLoginNeeded = false) public static Map
        login(@HttpMethod(prmName = "username") String un
                , @HttpMethod(prmName = "pw") String pw
                , @HttpMethod(prmName = "lastModified") Date lm
                ,@HttpMethod(prmName = "clientTime")Date clientTime
                , TL tl)throws Exception {
            User u=User.load(un);
            Map m= Util.mapCreate("time",tl.now);
            if(u != null && pw != null && u.pw!=null) {
                String b2 = Util.b64d(Util.b64d(pw))//b1=b64d(pw) YzMyODRkMGY5NDYwNmRlMWZkMmFmMTcyYWJhMTViZjM=
                        //b2=b64d(b1) c3284d0f94606de1fd2af172aba15bf3
                        ,m2Pm = Util.md5 (b2)
                        ,m2Db = Util.b64d(u.pw);//c3284d0f94606de1fd2af172aba15bf3
                if(m2Db.equals(m2Pm)) {
                    Prop e= Prop.load("user",u.id)
                            ,r=e.fk("role","roleId");
                    tl.h.s("usr", tl.usr = Util.mapCreate("osr",e,"user",u,"role",r));
                    Util.mapSet(m,"role",r,"user",e);
                }}//m1=md5("admin") 21232f297a57a5a743894a0e4a801fc3
            // m1b1=b64e(m1) MjEyMzJmMjk3YTU3YTVhNzQzODk0YTBlNGE4MDFmYzM=
            // m1b2=b64e(m1b1) TWpFeU16Sm1NamszWVRVM1lUVmhOelF6T0RrMFlUQmxOR0U0TURGbVl6TT0=  // WXpNeU9EUmtNR1k1TkRZd05tUmxNV1prTW1GbU1UY3lZV0poTVRWaVpqTT0
            // , m2= md5(m1):c3284d0f94606de1fd2af172aba15bf3
            // b1m2=b64(m2):YzMyODRkMGY5NDYwNmRlMWZkMmFmMTcyYWJhMTViZjM=
            return m;}

        @HttpMethod
        public static void logout(TL tl){ tl.usr =null;}
/*

    static Prop usrRole( TL tl){
        Object o=tl==null|tl.usr==null?null:tl.usr.get("role");
        OS e=o instanceof OS ?(OS )o:null;
        return e;}

    static int usrRoleLevel(TL tl){
        Prop r=usrRole(tl);
        Number l=r==null?null:r.propNum("level");
        return l==null?-1:l.intValue();
    }
        @HttpMethod
        public static List<Prop > poll(
                @HttpMethod(prmName = "lastModified")Date lastModified,
                @HttpMethod(prmName = "clientTime")Date clientTime, TL tl) {
            Object o=tl.usr==null?null:tl.usr.get("user");
            if(!(o instanceof User) || clientTime==null||lastModified==null)
                return null;
            User u=(User)o;
            Prop e=new Prop();int ur=usrRoleLevel(tl);
            boolean makeClones=true;
            List<Prop >l=new LinkedList<Prop >();
            long delta=clientTime.getTime()-lastModified.getTime();
            Date lm=new Date(tl.now.getTime()-delta);
            for(Sql.Tbl t:e.query( e.loadCols()
                    , Prop.where(Util.lst(
                            Prop.C.log//lastModified
                            , Dbg.Sql.Tbl.Co.ge)
                            ,lm)//, Prop.C.lastModified,Sql.Tbl.Co.max
                    , Prop.cols( Prop.C.objectStore, Prop.C.id, Prop.C.log)//lastModified
                    ,makeClones)
            )try
            {	Prop x=(Prop )t;
                if(x.checkAccess(u,ur,null,tl))
                    l.add(x);
            }catch(Exception ex){
                //tl.error(ex);
            }
            return l;}

        @Dbg.HttpMethod
        public static boolean dbSave(
                @Dbg.HttpMethod(prmName = "lastModified")Date lastModified,
                @Dbg.HttpMethod(prmName = "clientTime")Date clientTime,
                @Dbg.HttpMethod(prmName = "objectStore")String et,
                @Dbg.HttpMethod(prmName = "id")String id,
                @Dbg.HttpMethod(prmName = "jsonObj")Object obj, Dbg.TL tl
        )throws Exception {
            Prop b=dbSave(lastModified,clientTime,et,id,obj,null,null,tl);
            return b!=null;}

        public static Prop dbSave(
                Date lastModified, Date clientTime,
                String os, String id, Object obj,
                User usr, Integer usrRoleLevel, Dbg.TL tl)throws Exception{
            if(tl.usr==null)return null;
            if(usr==null)usr=(User)tl.usr.get("user");
            Map m=obj instanceof Map?(Map)obj:null;
            if(User.dbtName.equals(os) && m!=null){
                String un=(String)m.get("username")
                        ,pw=(String)m.get("pw")
                        ,p2= Dbg.Util.b64e(Dbg.Util.md5(Dbg.Util.b64d(Dbg.Util.b64d(pw))));
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
         * * /
        @Dbg.HttpMethod
        public static Map changes(
                @Dbg.HttpMethod(prmName = "changes")List l,
                @Dbg.HttpMethod(prmName = "clientTime")Date clientTime, Dbg.TL tl){
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
*/
//    }//class GrjSrvlt

//////////////////////////////////////////////////////////////////////
    /**Object Store Entity entry/row*/
    public static class Prop extends Dbg.Sql.Tbl {
        final static String dbtName="Prop";
        static Field[]Filds;

        @F public Integer id ;

        @F public Date log ;

        @F public String usr,domain,mac,path,prop,val;

 //       @Override public Field[] fields() { if(Filds==null) Filds=super.fields();return Filds; }

        @Override public String getName() {return dbtName;}

//        @Override public CI pkc(){return C.id;}@Override public Object pkv(){return id;}

        public enum C implements CI{id,log,usr,domain,mac,path,prop,val;
            @Override public Field f(){return Co.f(name(), Prop.class);}

            @Override public String getName() { return this.getName(); }

            @Override public Class getType() { return null; }
        }//C

        @Override public C[]columns(){return C.values();}

        @Override public Object[] wherePK() { return new Object[0]; }

        @Override public List DBTblCreation(Dbg.TL tl)
        //{ return null; }@Override public List creationDBTIndices()
        {
            final String V="varchar(255) NOT NULL DEFAULT '??' ";
            return Dbg.Util.lst
                    (Util.lst(
                            "int(36) not null primary key auto_increment"
                            ,"TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                            ,V,V,V,V,V,"text"
                            )
                            ,Util.lst("unique(`"+C.usr+"`,`"+C.mac+"`,`"+C.path+"`,`"+C.prop+"`)"
                            ,Util.lst(C.usr,C.log   )
                            ,Util.lst(C.usr,C.mac   ,C.log  )
                            ,Util.lst(C.usr,C.domain,C.log  )
                            ,Util.lst(C.usr,C.path  ,C.log  )
                            ,Util.lst(C.usr,C.prop  ,C.log  )
                            ,Util.lst(C.usr,C.path,C.prop,C.log )
                            )
                    );//val
		/*
		CREATE TABLE `Prop` (
		`id`	int(36) not null primary key auto_increment
		,`log`	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
		,`usr`	varchar(255) NOT NULL DEFAULT '??'
		,`domain`varchar(255) NOT NULL DEFAULT '??'
		,`mac`	varchar(255) NOT NULL DEFAULT '??'
		,`path`	varchar(255) NOT NULL DEFAULT '??'
		,`prop`	varchar(255) NOT NULL DEFAULT '??'
		,`val`	text
		,unique(`usr`,`mac`,`path`,`prop`)
		,index (`usr`,`log` )
		,index (`usr`,`mac`     ,`log`)
		,index (`usr`,`domain`  ,`log`)
		,index (`usr`,`path`    ,`log`)
		,index (`usr`,`prop`    ,`log`)
		,index (`usr`,`path`,`prop`,`log`)
		) ENGINE=InnoDB DEFAULT CHARSET=utf8;
		*/
        }

        static{registered.add(Prop.class);}

        @Override public Dbg.Sql.Tbl save() throws Exception {
            if(log==null)log=new Date();
            if(pool!=null)synchronized(pool) {
                super.save();
                saveLog();
                DB.D.close();
            }else
                log("Prop.save:no pool");
            return this;
        }//save

        void saveLog() throws Exception {
            Log g=Log.tl();g.vals( vals() );
            g.save();
        }//saveLog

        //public static void save(String usr,String domain,String mac,String path,String prop,String val)throws Exception{save(usr,domain,mac,path,prop,val,null);} //global.now

        public static void save(String usr,String domain,String mac,String path,String prop,SD d)throws Exception{//save(usr,domain,mac,path,prop,d.s,d.d);
            Prop m=Prop.tl();
            m.usr=usr;m.domain=domain;m.mac=mac;m.path=path;m.prop=prop;m.val=d.s;m.log=d.d;
            m.save();
        }

        Map<String,Map<String,SD>>loadProps(Map<String,Map<String,SD>>m
                ,String usr,String domain,String mac,Date log){
            if(m==null)m=new HashMap<String,Map<String,SD>>();
            for(Tbl t:query(
                    where(C.usr,usr
                            , C.domain,domain
                            , C.mac,mac
                            ,Util.lst( C.log,Co.gt,log),log )) )
            {	Map<String,SD>x=m.get( path );
                if(x==null)m.put(path,x=new HashMap<String,SD>());
                x.put( prop,new SD(val,log) );
            }
            return m;}

        List<Asic>loadAsicsProps(String usr,String domain,boolean isInitMacs){
            List<Asic>m=new LinkedList<Asic>();
            try{String sql="select `"+C.mac+"` from `"
                    +dbtName+"` where `"+C.usr+"`=? and  `"+C.domain
                    +"`=? group by `"+C.mac+"`";//Object[]a=D.q1col( sql,usr,domain );
                List<String>a=D.q1colTList(sql,String.class,C.usr,usr,C.domain,domain);

                for(String mac:a)try{
                    //String mac=o==null?null:o.toString();if(mac==null)continue;
                    Asic x=isInitMacs?Asic.macs.get( mac):new Asic( mac );
                    boolean exists=x!=null;
                    if(isInitMacs)Asic.macs.put( mac,exists?x:(x=new Asic( mac ) ));
                    else if(!exists)x=new Asic( mac );
                    m.add( x );
                    if(!isInitMacs||!exists)
                        x.vals=loadProps(x.vals, usr,domain,mac,new Date(0) );
                    else//TODO: merge db-vals with live-vals
                        x.mergeProps(loadProps(null, usr,domain,mac,new Date(0) ));
                }catch ( Exception x ){
                    error(x,"Json.DB.Prop.loadAsicsProps:for:");
                }
            }catch ( Exception x ){
                error(x,"Json.DB.Prop.loadAsicsProps:");
            }
            return m;
        }

        public static Prop tl(){
            Json t=Json.tl();
            if(t.prop==null){
                t.prop=new Prop();Log.tl();check();//x.checkDBTCreation();
            }
            return t.prop;
        }

        @Override public Prop newInst(){return new Prop();}

        public static class SD{public String s;public Date d;public SD(String a,Date b){s=a;d=b;}}

    } // class Prop extends Tbl

    public static class Log extends Prop{

        @Override public String getName() {
            return "Log";}

        void saveLog() throws Exception {}//saveLog

        @Override public List creationDBTIndices(){
            List x=super.creationDBTIndices()
                    ,z=(List)x.get( 1 );
            z.remove( 0 );
            return x;
        }

        static{registered.add(Log.class);}//public static Log sttc=new Log( );

        public static Log tl(){
            Json t=Json.tl();
            if( t.log==null){
                t.log=new Log();check();
            }
            return t.log;
        }
        @Override public Log newInst(){return new Log();}

    } // class Log extends Prop extends Tbl

//////////////////////////////////////////////////////////////////////

    public static class User extends Prop{ }//class User //  @Override public Object[]wherePK(){Object[]a={C.id,id};return a;}



}// AsicsSrvlt
