# RemoveButterKnife
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RemoveButterKnife-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3750)
## What's the plugin
An Android Studio plugin to help remove use of ButterKnife

## Why i do this
ButterKnife is a wellknow open source tool to help you inject your view,  but some times, you use plugin to gen butterknife's code, but you
don't want to use it again. 

It's a very horrible job to delete every line inject code and write findviewbyid code, so, i made this plugin to do it.

## How to use it
(You can search it on your AS plugin install page, name is RemoveButterKnife)

1.Clone the code and build it in your  idea

2.Then, you can find a jar, install it as a plugin in your AndroidStudio**(You also can find the jar in the project)**

3.Open your Activity/Fragment you want to change.

4.Find the RemoveButterKnife button in your android studio edit menu

![](http://www.u3coding.com/wp-content/uploads/2016/06/1.gif)

## The idea of how to design and implement it
You can see this blog

[中文版](http://www.u3coding.com/2016/06/24/androidstudio-plugin-removebutterknife-di/)

[English](http://www.u3coding.com/2016/06/26/androidstudio-plugin-dev-removebutterknife-design-to-implement/)

## License
See the LICENSE file for license rights and limitations(GNU3.0)

## NextStep
##### Use annotation processor and javapoet to rebuild
##### Support onclick annotation
##### On the way
