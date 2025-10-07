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
**tornado CLI:**

```bash
tornado --jvm="-Dorg.slf4j.simpleLogger.defaultLogLevel=off \
-Dtornado.device.memory=20GB -XX:MaxDirectMemorySize=20G" \
-cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" \
org.example._1_ChatExample
```

**java**
```bash
java @options.txt -cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" org.example._1_ChatExample
```

### 2. Streaming Example  
**tornado CLI:**

```bash
tornado --jvm="-Dorg.slf4j.simpleLogger.defaultLogLevel=off \
-Dtornado.device.memory=20GB -XX:MaxDirectMemorySize=20G" \
-cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" \
org.example._2_StreamingExample
```

**java**
```bash
java @options.txt -cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" org.example._2_StreamingExample
```

### 3. Chat Memory Example  
**tornado CLI:**

```bash
tornado --jvm="-Dorg.slf4j.simpleLogger.defaultLogLevel=off \
-Dtornado.device.memory=20GB -XX:MaxDirectMemorySize=20G" \
-cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" \
org.example._3_ChatMemoryExample
```

**java**
```bash
java @options.txt -cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" org.example.agents._1_basic_agent.org.example._3_ChatMemoryExample
```

## Agent Examples 
###### !!! Port of [Agentic Tutorial](https://github.com/langchain4j/langchain4j-examples/tree/main/agentic-tutorial) for GPULlama3.java !!!

### 1a. Basic Agent Example
**tornado CLI:**

```bash
tornado --jvm="-Dorg.slf4j.simpleLogger.defaultLogLevel=off \
-Dtornado.device.memory=20GB -XX:MaxDirectMemorySize=20G" \
-cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" \
org.example.agents._1_basic_agent._1a_Basic_Agent_Example <GPU|CPU>
```

**java**
```bash
java @options.txt -cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" org.example.agents._1_basic_agent._1a_Basic_Agent_Example  GPU
```


### 1b. Basic Agent Example (Structured)  
**tornado CLI:**

```bash
tornado --jvm="-Dorg.slf4j.simpleLogger.defaultLogLevel=off \
-Dtornado.device.memory=20GB -XX:MaxDirectMemorySize=20G" \
-cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" \
org.example.agents._1_basic_agent._1b_Basic_Agent_Example_Structured <GPU|CPU>
```

**java**
```bash
java @options.txt -cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" org.example.agents._1_basic_agent._1b_Basic_Agent_Example_Structured  GPU
```

### 2a. Sequential Agent Example  
**tornado CLI:**

```bash
tornado --jvm="-Dorg.slf4j.simpleLogger.defaultLogLevel=off \
-Dtornado.device.memory=20GB -XX:MaxDirectMemorySize=20G" \
-cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" \
org.example.agents._2_sequential_workflow._2a_Sequential_Agent_Example <GPU|CPU>
```

**java**
```bash
java @options.txt -cp "target/langchain4j-gpullama3-demo-1.0-SNAPSHOT.jar:$(cat cp.txt)" org.example.agents._2_sequential_workflow._2a_Sequential_Agent_Example  GPU
```

## Performance Comparison: GPULlama3.java Engines

The plots compare **GPULlama3.java running on two different execution engines**:

- **GPU Engine (TornadoVM OpenCL)** on *Nvidia 5090 – 24 GB*  
- **CPU Engine (llama3.java)<sub>1</sub>**  on *Intel® Core™ Ultra 9 275HX × 24, 64 GB*  

The bar charts show **GPU speedups over CPU** for different tasks and models.  
- Across all benchmarks, the **GPU engine consistently outperforms the CPU engine**, with speedups ranging from ~3.5× to nearly 5× depending on the model and workload.  
- The **task-level plots** highlight where GPU acceleration provides the largest gains, while the **average speedup plots** summarize overall performance advantages per model.  

These results demonstrate the significant benefits of running GPULlama3 models on TornadoVM’s GPU engine compared to the CPU engine.

<img width="6000" height="3600" alt="gpu_cpu_speedup_final" src="https://github.com/user-attachments/assets/b4f2ec51-b025-4f0e-9fde-7e8c0bf926d1" />
<sub>1</sub>: It was developed from the commit point commit https://github.com/mukel/llama3.java/commit/5fc76c665c349456e1a6a458339531bf3abab308

