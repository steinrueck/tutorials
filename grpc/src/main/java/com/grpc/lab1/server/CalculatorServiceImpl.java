package com.grpc.lab1.server;

import com.grpc.lab1.BlockRequest;
import com.grpc.lab1.BlockReply.Row;

import java.util.Arrays;

import com.grpc.lab1.BlockReply;
import com.grpc.lab1.CalculatorServiceGrpc.CalculatorServiceImplBase;

import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceImplBase
{
    public static int MAX = 4; 
    
    @Override
    public void multiplyBlock(BlockRequest request, StreamObserver<BlockReply> reply)
    {
        System.out.println("multiplyBlock -> Request received from client:\n" + request);
        System.out.println("now doing stuff on the server");
        int A[][] = new int [MAX][MAX];
        int B[][] = new int [MAX][MAX];
        for (int i = 0; i < request.getACount(); i++) {
            for (int j = 0; j < request.getA(i).getColumnsCount(); j++) {
                A[i][j] = request.getA(i).getColumns(j);
                B[i][j] = request.getB(i).getColumns(j);
            }
        }
        System.out.println("Arrays A and B have been filled.");
    	
        int C[][]= new int[MAX][MAX];
    	C[0][0]=A[0][0]*B[0][0]+A[0][1]*B[1][0];
    	C[0][1]=A[0][0]*B[0][1]+A[0][1]*B[1][1];
    	C[1][0]=A[1][0]*B[0][0]+A[1][1]*B[1][0];
    	C[1][1]=A[1][0]*B[0][1]+A[1][1]*B[1][1];

        System.out.println("Blocks A and B have been multiplied. Result is C.");

        BlockReply.Builder responseBuilder = BlockReply.newBuilder();
        Row.Builder rowBuilder = Row.newBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                rowBuilder.addColumns(C[i][j]);
            }
            responseBuilder.addC(rowBuilder);
            rowBuilder = Row.newBuilder();
        }
        BlockReply response = responseBuilder.build();
        System.out.println("Sending response to the client...");
        reply.onNext(response);
        reply.onCompleted();
    }

    @Override
    public void addBlock(BlockRequest request, StreamObserver<BlockReply> reply)
    {
        System.out.println("addBlock: -> Request received from client:\n" + request);
        System.out.println("now doing stuff on the server");
        int A[][] = new int [MAX][MAX];
        int B[][] = new int [MAX][MAX];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                A[i][j] = request.getA(i).getColumns(j);
                B[i][j] = request.getB(i).getColumns(j);
            }
        }
        System.out.println("Arrays A and B have been filled.");
    	
        int C[][]= new int[MAX][MAX];
    	for (int i=0;i<C.length;i++)
    	{
    		for (int j=0;j<C.length;j++)
    		{
    			C[i][j]=A[i][j]+B[i][j];
    		}
    	}

        System.out.println("Blocks A and B have been multiplied. Result is C.");

        BlockReply.Builder responseBuilder = BlockReply.newBuilder();
        Row.Builder rowBuilder = Row.newBuilder();
    	for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                rowBuilder.addColumns(C[i][j]);
            }
            responseBuilder.addC(rowBuilder);
            rowBuilder = Row.newBuilder();
        }
        BlockReply response = responseBuilder.build();
        System.out.println("Sening the response to the client...");
        reply.onNext(response);
        reply.onCompleted();
    }
}

// public class CalculatorServiceImpl extends CalculatorServiceImplBase
// {
//   @Override
//   public void add(AddRequest request, StreamObserver<AddReply> reply)
//   {
//      System.out.println("Request received from client:\n" + request);
//      AddReply response=AddReply.newBuilder().setC(request.getA()+request.getB()).build();
//      reply.onNext(response);
//      reply.onCompleted();
//   }
// }