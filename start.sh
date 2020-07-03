java -jar ../target/watchdog-1.0-SNAPSHOT.jar --logLevel info \
	--ssl --host api.huobiasia.vip \
	--heartBeat '{"pong":%s}' \
	--subscribe '{"sub":"market.btcusdt.kline.1min"}' \
	--market hb \
	--marketConfig '{"candleChangeRatioConditions":{"btcusdt":0.8}}' \
	--barkIds XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
