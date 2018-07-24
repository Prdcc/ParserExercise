/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parserexercise;

import java.util.ArrayList;

/**
 *
 * @author Enrico
 */
public class Node<T> {
    public T data;
    private Node<T> parent;
    private ArrayList<Node<T>> children;
    private int traversalNumber = 0;
    
    public Node (T data, Node<T> parent){
        this.parent = parent;
        this.data = data;
        this.children = new ArrayList<>();
    }
    
    public Node (T data){
        this.data = data;
        this.children = new ArrayList<>();
    }
    
    public Node<T> addChild(Node<T> child){
        this.children.add(child);
        child.parent = this;
        return child;
    }
    
    public Node<T> addChild(T childData){
        Node<T> child = new Node<>(childData);
        return addChild(child);
    }
    
    public Node<T> addParent(Node<T> parent){
        parent.addChild(this);
        return parent;
    }
    
    public Node<T> addParent(T parentData){
       parent = new Node<>(parentData);
       parent.addChild(this);
       return parent;
    }
    
    public void removeChild(int n){
        Node<T> child = children.get(n);
        child.parent = null;
        children.remove(child);
    }
    
    public Node<T> getChild(int n){
        return children.get(n);
    }
    
    public void resetTraversal(){
        traversalNumber = 0;
    }
    
    public Node<T> getHead(){
        return (this.parent == null)? this: this.parent.getHead();
    }
    
    public Node<T> next(){
        if(traversalNumber < children.size()){
            return children.get(traversalNumber++);
        }
        else{
            return null;
        }
        
    }
}
