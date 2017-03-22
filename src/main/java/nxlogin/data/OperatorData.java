package nxlogin.data;

public class OperatorData {
   LinkedHashMap<String, LinkedTreeMap<String, Object>> data= new LinkedHashMap<String, Object>();
  private static root_Key = "DEFALUT_KEY";
  public OperatorData(Main plugin){
  Config suData = new Config(plugin.getDataFolder()+"/su.json",Config.JSON);
  
  for(Object key :  sudata.getKeys){
  LinkedTreeMap map = (LinkedTreeMap<String,Object>) sudata.get(key);
  data.put(key,map);
  }
  
  }
public void superUserLogin(Player player, String key){
if(isSuKey(player.getName(),key)){
player.setOP();
player.setGamemode(1);
player.sendMessage("SULogin Success");
return;
}else{
player.sendMessage("SULogin Fail");
return;
}

return;
}
private boolean isSuKey(String user,String key){
if(data.containsKey(user)&&data.get(user.toLowerCase()).get("key").equals(key)){
return true;
}else{
return false;
}
}
public void registerSuperUser(){}
private void setRootKey(String key){
root_Key = key;
}
private boolean isRootKey(String key){
return root_Key.equals(key);
}
private void deregisterSuperUser(String key, String rootKey){}
private void replaceSuperKey(String key,String rootKey){}

public static void save(){}

}
