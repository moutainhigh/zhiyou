JAVA_OPTS="
-Djava.security.egd=file:/dev/./urandom
-server
-Xms8g
-Xmx8g
-Xss256k
-XX:+UseCompressedOops
-XX:+AggressiveOpts
-XX:+UseFastAccessorMethods
-XX:+UseStringDeduplication
-XX:+UseG1GC
-XX:+OptimizeStringConcat
-XX:CICompilerCount=8
-XX:+ScavengeBeforeFullGC
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/opt/heapError/
"

