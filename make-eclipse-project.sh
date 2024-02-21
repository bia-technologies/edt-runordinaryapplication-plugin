basepath=./dev/eclipse_project
mkdir -p $basepath
echo Clear project catalog
rm -rf $basepath/*
echo Copy project template
cp -r ./templates/eclipse_project/* $basepath
echo Make source symlinks
cd $basepath/bundles/

echo Create symlink for 'core'
rm -rf ./core
ln -s $s ../../../core

echo Create symlink for 'ui'
rm -rf ./ui
ln -s $s ../../../ui

cd ../../..