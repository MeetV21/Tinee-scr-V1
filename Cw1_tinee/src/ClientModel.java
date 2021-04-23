import java.io.IOException;
import java.util.ArrayList;
import sep.mvc.AbstractModel;
import sep.tinee.net.channel.ClientChannel;
import sep.tinee.net.message.Message;
import sep.tinee.net.message.Push;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * 
 */
public class ClientModel extends AbstractModel {

  ClientModel(String user,String host,int port){
    super(user,host,port);
  }

  @Override
   public void send(Message msg) throws IOException {
    this.channel.send(msg);
  }

  @Override
  public Message receive() throws IOException, ClassNotFoundException {
    return this.channel.receive();
  }

  @Override
  public void addDraftLine(String line){
    this.draftLines.add(line);
  }

  @Override
  public void removeDraftLine(String line){
    this.draftLines.remove(line);
  }

  @Override
  public Push createPushCommand(String tag) {
    return new Push(this.user,tag,this.draftLines);
   }
}