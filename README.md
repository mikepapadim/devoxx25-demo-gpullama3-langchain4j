t to doo 





bash c

1) tornado --jvm="-Dorg.slf4j.simpleLogger.defaultLogLevel=off -Dtornado.device.memory=20GB -XX:MaxDirectMemorySize=20G" -cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" org.example._1_ChatExample



2)tornado --jvm="-Dorg.slf4j.simpleLogger.defaultLogLevel=off -Dtornado.device.memory=20GB -XX:MaxDirectMemorySize=20G" -cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" org.example._2_ChatMemoryExample



3) tornado --jvm="-Dorg.slf4j.simpleLogger.defaultLogLevel=off -Dtornado.device.memory=20GB -XX:MaxDirectMemorySize=20G" -cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" org.example._3_StreamingExample


4)tornado --jvm="-Dorg.slf4j.simpleLogger.defaultLogLevel=off -Dtornado.device.memory=20GB -XX:MaxDirectMemorySize=20G" -cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" org.example._4_Conversation


