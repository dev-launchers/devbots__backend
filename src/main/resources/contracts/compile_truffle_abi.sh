cd truffle_compile
for file in *; do jq .abi $file >> ../${file}_abi; done 
