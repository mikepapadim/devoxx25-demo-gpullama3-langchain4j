# Devoxx 2025 Demo – GPULLaMA3.java with LangChain4j & TornadoVM  

This repository contains demo applications showcasing how to run **LLaMA 3 models on GPU** via [LangChain4j](https://github.com/langchain4j/langchain4j), using [GPULlama3.java](https://github.com/beehive-lab/GPULlama3.java) and integrated with [TornadoVM](https://github.com/beehive-lab/TornadoVM).  

The examples demonstrate:  
- Basic chat interaction  
- Memory-enabled conversations  
- Streaming responses  
- Multi-turn conversational agents  

---

## 🚀 Prerequisites  

- **Java 21+**  
- **Maven** (to build the project)  
- **TornadoVM** installed and configured on your system  
- A GPU with sufficient VRAM (20 GB recommended)  

Follow the [TornadoVM installation guide](https://github.com/beehive-lab/TornadoVM) to set up TornadoVM on your system.
Make sure TornadoVM is available in your `PATH` via the `tornado` command.  

---

## ⚙️ Build  

Run the following to build the project **and** generate the classpath file (`cp.txt`):  

```bash
mvn clean package && mvn dependency:build-classpath -Dmdep.outputFile=cp.txt
```

This produces:  

- `target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar`  
- `cp.txt` (classpath file used in the run commands)  

---

## ▶️ Run the Examples  

All commands assume you are in the project root and have a valid `cp.txt` (classpath dependencies file).  

### 1. Chat Example  

```bash
tornado --jvm="-Dorg.slf4j.simpleLogger.defaultLogLevel=off \
-Dtornado.device.memory=20GB -XX:MaxDirectMemorySize=20G" \
-cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" \
org.example._1_ChatExample
```

### 2. Streaming Example  

```bash
tornado --jvm="-Dorg.slf4j.simpleLogger.defaultLogLevel=off -Dtornado.device.memory=20GB -XX:MaxDirectMemorySize=20G"
-cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)"
org.example._2_StreamingExample
```

### 3. Chat Memory Example  

```bash
bash tornado --jvm="-Dorg.slf4j.simpleLogger.defaultLogLevel=off -Dtornado.device.memory=20GB -XX:MaxDirectMemorySize=20G"
-cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)"
org.example._3_ChatMemoryExample
```

