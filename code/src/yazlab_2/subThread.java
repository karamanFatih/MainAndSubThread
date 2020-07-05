package yazlab_2;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.ASCIICaseInsensitiveComparator;

public class subThread extends Thread {

    
    //private Lock lock1 = new ReentrantLock();
    Object lock = new Object();
    Random random = new Random();
    private static int say = 0, say1 = 0;
    private static int subThreadIstek = 0;
    private String isim;

    
    
    
    

    public void subThread() {
        Yazlab_2 y = new Yazlab_2();
        Thread subThread_istek_alici = new Thread(new Runnable() {

            @Override
            public void run() {         // mainThreaden İstek alım yükünü hafifletme***************************************************************

                while (true) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    synchronized (lock) {

                        if (subThreadIstek >= 5000) {

                            try {
                                System.out.println("SubThread2 kapasitesi doldu biraz bekleyiniz!");
                                lock.wait();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }

                        Integer value = random.nextInt(49) + 1;

                        if (value <= y.getMainThreadIstek()) {
                            System.out.println("MainThread kuyrugundaki istek sayısı :                                                                    " + y.getMainThreadIstek());
                            y.getLock2().lock();
                            y.setMainThreadIstek(y.getMainThreadIstek() - value);
                            y.getLock2().unlock();
                            subThreadIstek += value;

                            if (subThreadIstek > 5000) {
                                value = subThreadIstek - 5000;

                                subThreadIstek = 5000;

                            }

                            System.out.println("subThread2 altsunucu   " + value + " adet isteği subThread2 kuyruğuna ekledi. subThread2 kuyruğunda bekleyen istek toplam " + subThreadIstek + " oldu.");
                            say = 1;
                        } else {
                            if (say != 0) {
                                subThreadIstek += y.getMainThreadIstek();
                                System.out.println("subThread2 altsunucu   " + y.getMainThreadIstek() + " adet isteği subThread1 kuyruğa ekledi. subThread2 kuyrukta bekleyen istek toplam " + subThreadIstek + " oldu.");

                                //System.out.println("Main sunucu  " + istek + " adet isteği kuyruga aldı.");
                                y.getLock2().lock();
                                y.setMainThreadIstek(0);
                                y.getLock2().unlock();
                                System.out.println("MainThread kuyrugundaki istek sayısı :                                                                   " + y.getMainThreadIstek());

                                say = 0;
                            }
                        }
                        lock.notify();

                    }

                }

            }

        });

        Thread subThread_istek_cevaplayici = new Thread(new Runnable() {

            @Override
            public void run() {//subThread2 istek yanıt threadi

                while (true) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    synchronized (lock) {

                        if (subThreadIstek <= 0) {

                            try {
                                lock.wait();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        //Random random = new Random();

                        Integer value1 = random.nextInt(49) + 1;

                        if (subThreadIstek > value1) {

                            subThreadIstek -= value1;
                            System.out.println("subThread2 altsunucu kurukta bekleyen " + value1 + " adet istek cevaplandı.");
                            System.out.println("subThread2 kurukta bekleyen islem sayısı : " + subThreadIstek);
                            say1 = 1;
                            //queue.remove(value1);
                        } else {
                            if (say1 != 0) {
                                System.out.println("subThread2 altsunucu kurukta bekleyen " + subThreadIstek + " adet istek cevaplandı.");

                                subThreadIstek = 0;

                                System.out.println("subThread2 kurukta bekleyen islem sayısı : " + subThreadIstek);
                                say1 = 0;
                            }
                        }
                        if (subThreadIstek < 5000) {
                            lock.notify();
                        }

                    }

                }

            }

        });

        subThread_istek_alici.start();
        subThread_istek_cevaplayici.start();
        try {

            subThread_istek_alici.join();
            subThread_istek_cevaplayici.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    public subThread(String threadIsim) {
        this.isim = isim;
        //new subThread(threadIsim).subThread();
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

}
