```bash
dos2unix network.config scripts/*.sh
chmod +x scripts/*.sh
taskkill /F /IM java.exe 2>NUL
rm -rf pids logs pipes; mkdir pids logs pipes
export CLASSPATH="target/classes"
READY_TIMEOUT=20 ./scripts/scenario1.sh
```
