package dxn;
/**
 * Represents a queue of theads. Allows them to be runs them in a first in, first out pattern.
 * 
 * @author  Michelle Huang
 */
public class ThreadQueue implements Runnable
{
   private Thread[] queue;
   private boolean isDone;
  
    /**
    * Default, no-arg constructor.
    */
   public ThreadQueue()
   {
       queue = new Thread[0];
       isDone = false;
   }
   
   /**
    * Adds a thread to the end of the queue
    * @param extra  The thread to be added
    */
   public void addThread(Thread extra)
   {
       Thread[] newQueue = new Thread[1+queue.length];
        for( int i = 0; i<queue.length; i++)
        newQueue[i] = queue[i];
        newQueue[queue.length]=extra;
        queue = newQueue;
        isDone = false;
   }
   /**
    * Adds a thread to the end of the queue. ThreadQueue automatically turns the Runnable object
    * into a new thread.
    * @param object The runnable object to be added
    */
   public void addThread(Runnable object)
   {
       Thread newThread = new Thread(object);
       addThread(newThread);
   }
   
   /**
    * Removes the first thread in the queue. The second-in line becomes the new first.
    */
   public void removeFirstThread()
   {    
        Thread[] newQueue = new Thread[queue.length-1];
        for( int i = 0; i<queue.length-1; i++)
        newQueue[i] = queue[i+1];
        queue = newQueue;
   }
   
   /**
    * Runs each thread, starting from the first until and ending on the last
    */
   public void runQueue()
   {
        while(queue.length !=0)
        {
            queue[0].start();
            try{
            queue[0].join();
            }catch(InterruptedException e){}
            removeFirstThread();
        }
        isDone = true;
   }
   /**
    * Returns true if the queue is empty and false if there are threads left to run.
    * @return   boolean isDone
    */
   public boolean isDone()
   {
       return isDone;
    }
   
   
   public Thread threadedQueue;
   /**
    * Runs the queue in its own separate thread
    * @see run()
    */
   public void runQueueT()//runs the queue in a new thread
   {
       threadedQueue = new Thread(this);
       threadedQueue.start();
   }
   /**
    * Used in conjunction with runQueueThreaded(). Required run method for thread starting.
    * @see runQueueThreaded()
    */
   public void run()
   {
       runQueue();
   }

}


