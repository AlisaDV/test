mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    target/cyber.jar \
    ubuntu@213.219.214.7:/home/ubuntu/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa_ubuntu ubuntu@213.219.214.7 << EOF
pgrep java | xargs kill -9
nohup java -jar cyber.jar > log.txt &
EOF

echo 'Bye'
