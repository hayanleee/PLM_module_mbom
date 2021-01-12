package com.yura.mbom.service;

import java.util.Locale;

import matrix.db.BusinessObject;
import matrix.db.Context;
import matrix.db.Relationship;
import matrix.db.RelationshipItr;
import matrix.db.RelationshipList;

public class DomainUtil {

	public DomainUtil() {}

    /**
     * Context 획득
     * @throws Exception
     * @return Context
     */
    public static Context getContext() throws Exception {
        Context context = new Context( ":bos", null );
        context.setLocale( Locale.KOREAN );
        context.setUser("Test Everything");
        context.setPassword("");
        context.connect();
        return context;
    }

    public static Context getContext(String port) throws Exception {
        Context context = new Context("http://plm.yura.co.kr:"+port+"/enovia");
        context.setLocale( Locale.KOREAN );
    	context.setUser("Test Everything");
    	context.setPassword("");
    	context.connect();
        return context;
    }

    public static Context getContext(String sHOST, String sServer, String sUser, String sPasswd) throws Exception {
        Context context = new Context( sHOST, sServer );
        context.setLocale( Locale.KOREAN );
        context.setUser(sUser);
        context.setPassword(sPasswd);
        context.connect();
        return context;
    }


    /**
     * fromObjectID 객체와 toObjectID 객체가 relationshipTypeName 으로 이미 연결 되었는지 check 해서
     * 연결되었으면 true return, 연결되지 않았으면 false return
     * @param  context eMatrix Context
     * @param  fromObjectID 연결할 from 객체 ID
     * @param  toObjectId 연결할 to 객체 ID
     * @param  relationshipTypeName Relationship name
     * @return  이미 연결되었으면 true return, 연결되지 않았으면 false return
     * @throws Exception
     */
    public static boolean isAlreadyConnected( Context context, String fromObjectID,
                                              String toObjectID, String relationshipTypeName ) throws Exception {
        BusinessObject source = new BusinessObject( fromObjectID );
        BusinessObject target = new BusinessObject( toObjectID );
        return isAlreadyConnected( context, source, target, relationshipTypeName );
    }

	/** source 객체와 target 객체가 relationshipTypeName 으로 이미 연결 되었는지 check 해서
     * 연결되었으면 true return, 연결되지 않았으면 false return
     * @param  context eMatrix Context
     * @param  source 연결할 from 객체
     * @param  target 연결할 to 객체
     * @param  relationshipTypeName Relationship name
     * @return  이미 연결되었으면 true return, 연결되지 않았으면 false return
     * @throws Exception
     */
    public static boolean isAlreadyConnected( Context context, BusinessObject source,
                                              BusinessObject target, String relationshipTypeName ) throws Exception {

        target.open( context );
        RelationshipList targetRelationList = target.getToRelationship( context );
        target.close( context );

        RelationshipItr targetRelationItr = new RelationshipItr( targetRelationList );
        while( targetRelationItr.next() ) {
            Relationship relation = targetRelationItr.obj();
            String typeName = relation.getTypeName();
            if( !typeName.equals( relationshipTypeName ) ) {
                continue;
            }
            BusinessObject otherSideObj = relation.getFrom();
            source.open( context );
            if( source.equals( otherSideObj ) ) {
                return true; // 이미 연결된 객체들
            }
            source.close( context );
        }
        return false;
    }

}

