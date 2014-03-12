/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.jms;

import javax.jms.*;
import javax.naming.*;

public class LPQueueListener implements MessageListener
{
	  public LPQueueListener() throws Exception
	  {
	    final String MEINE_QUEUE = "queue/LPQueue";
	    Context         ctx      = null;
	    QueueConnection connect  = null;
	    QueueSession    session  = null;
	    Queue           queue    = null;
	    QueueReceiver   receiver = null;
	    try {
	      ctx = new InitialContext();
	      QueueConnectionFactory fact = (QueueConnectionFactory)
	                                    ctx.lookup( "ConnectionFactory" );
	      connect = fact.createQueueConnection();
	      session = connect.createQueueSession( false, Session.AUTO_ACKNOWLEDGE );
	      try {
	        queue = (Queue) ctx.lookup( MEINE_QUEUE );
	      } catch( NameNotFoundException ex ) {
	        queue = session.createQueue( MEINE_QUEUE );
	        ctx.bind( MEINE_QUEUE, queue );
	      }
	      receiver = session.createReceiver( queue );
	      receiver.setMessageListener(this);
	      connect.start();
	     //7 Thread.sleep( 20000 );
	    } finally {
	    // try { if( null != receiver ) receiver.close(); } catch( Exception ex ) {}
	    //  try { if( null != session  ) session.close();  } catch( Exception ex ) {}
	    //  try { if( null != connect  ) connect.close();  } catch( Exception ex ) {}
	    //  try { if( null != ctx      ) ctx.close();      } catch( Exception ex ) {}
	    }
	  }

	  public void onMessage( Message message )
	  {
	    try {
	      //TextMessage msg = (TextMessage) message;
	      System.out.println(message.toString() );
	      message.acknowledge();
	    } catch( JMSException ex ) {
	      System.out.println( ex.getMessage() );
	    }
	  }
}