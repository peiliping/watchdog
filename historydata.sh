subs=`echo "" | awk '
BEGIN{
  f=1593532800
}
{
  for(i=0;i<62;i++){
    k1=f+43200*i;
    k2=f+43200*(i+1);
    r=r"{\"req\":\"market.btcusdt.kline.1min\",\"symbol\":\"btcusdt\",\"period\":\"1min\",\"from\":"k1",\"to\":"k2"}$"
  }
}
END{
  print r
}
'`

echo $subs

java -jar target/watchdog-1.0-SNAPSHOT.jar \
	--logLevel debug \
	--ssl --host api.huobiasia.vip \
	--market hb \
	--heartBeat '{"pong":%s}' \
	--subscribe $subs 

## grep Binary watchdog.log  | grep -v ping | awk '{print $11}' > /tmp/history.log
