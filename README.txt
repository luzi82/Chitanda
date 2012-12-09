cd ${PROJECT_ROOT}

pushd Chitanda-common
mkdir import
cd import
wget http://mirrors.devlib.org/apache//commons/collections/source/commons-collections-3.2.1-src.tar.gz
tar -xzvf commons-collections-3.2.1-src.tar.gz
popd

pushd Chitanda
ln -s ../Chitanda-android/assets/ ./
popd

pushd Chitanda-server-blockbreak
mkdir import
cd import
ln -s ../../Chitanda-common/import/commons-collections-3.2.1-src commons-collections
popd
