package yazlab_2;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.ASCIICaseInsensitiveComparator;

public class Yazlab_2 implements  Runnable{

    private Lock lock2 = new ReentrantLock();

    private static int istek = 0;
    Object lock = new Object();
    Object lock1 = new Object();
    Object lock3 = new Object();
    private static int mainThreadIstek = 0;

    Random random = new Random();
    private static int say = 0, say1 = 0, st1say = 0, st1say1 = 0, st2say = 0, st2say1 = 0;
    private static int subThreadIstek = 0;
    private static int subThreadIstek2 = 0;
    subThread s = new subThread("a");

    public void mainSunucu() {

        s.subThread();

        Thread main_istek_alici = new Thread(new Runnable() {
            @Override
            public void run() {         // mainThread İstek kabul threadi***************************************************************

                while (true) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    synchronized (lock) {

                        if (mainThreadIstek >= 10000) {

                            try {
                                System.out.println("Main sunucu kapasitesi doldu biraz bekleyiniz!");
                                lock.wait();
                                //System.out.println("bura çalıştı!   1. lock wait() 1.tread");
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }

                        Integer value = random.nextInt(99) + 1;

                        if (value <= istek) {
                            System.out.println("kalan istek sayısı :                                                                    " + istek);

                            istek -= value;
                            lock2.lock();
                            mainThreadIstek += value;
                            lock2.unlock();
                            if (mainThreadIstek > 10000) {
                                value = mainThreadIstek - 10000;
                                lock2.lock();
                                mainThreadIstek = 10000;
                                lock2.unlock();

                            }

                            System.out.println("Main sunucu   " + value + " adet isteği kuyruğa ekledi. kuyrukta bekleyen istek toplam " + mainThreadIstek + " oldu.");
                            say = 1;
                        } else {
                            if (say != 0) {
                                lock2.lock();
                                mainThreadIstek += istek;
                                lock2.unlock();
                                System.out.println("Main sunucu   " + istek + " adet isteği kuyruğa ekledi. kuyrukta bekleyen istek toplam " + mainThreadIstek + " oldu.");

                                //System.out.println("Main sunucu  " + istek + " adet isteği kuyruga aldı.");
                                istek = 0;
                                System.out.println("kalan istek sayısı :                                                                    " + istek);

                                say = 0;
                            }
                        }
                        //System.out.println("bura çalıştı!   1. lock notify() 1.tread");
                        lock.notify();

                    }

                }

            }
        });
        Thread main_istek_cevaplayici = new Thread(new Runnable() {
            @Override
            public void run() {         // MainThread istek yanıt threadi*******************************************************************

                while (true) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    synchronized (lock) {

                        if (mainThreadIstek <= 0) {

                            try {
                                //System.out.println("bura çalıştı!   2. lock wait() 2.tread");
                                lock.wait();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }

                        Integer value1 = random.nextInt(49) + 1;

                        if (mainThreadIstek > value1) {
                            lock2.lock();
                            mainThreadIstek -= value1;
                            lock2.unlock();
                            System.out.println("Main sunucu kurukta bekleyen " + value1 + " adet istek cevaplandı.");
                            System.out.println("kurukta bekleyen islem sayısı : " + mainThreadIstek);
                            say1 = 1;
                            //queue.remove(value1);
                        } else {
                            if (say1 != 0) {
                                System.out.println("Main sunucu kurukta bekleyen " + mainThreadIstek + " adet istek cevaplandı.");
                                lock2.lock();
                                mainThreadIstek = 0;//System.out.println("bura çalıştı!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
                                lock2.unlock();
                                System.out.println("kurukta bekleyen islem sayısı : " + mainThreadIstek + "");
                                say1 = 0;
                            }
                        }
                        // System.out.println("bura çalıştı!   2. lock notify() 2.tread");

                        if (mainThreadIstek < 10000) {
                            lock.notify();
                        }

                    }

                }

            }
        });

        /*
         * ****************************************************************sub**************************************************************************
         *
         */
        Thread subThread1_istek_alici = new Thread(new Runnable() {

            @Override
            public void run() {         // mainThreaden İstek alım yükünü hafifletme***************************************************************

                while (true) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    synchronized (lock1) {

                        if (subThreadIstek >= 5000) {

                            try {
                                System.out.println("SubThread1 kapasitesi doldu biraz bekleyiniz!");
                                lock1.wait();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }

                        Integer value = random.nextInt(49) + 1;

                        if (value <= mainThreadIstek) {
                            System.out.println("MainThread kuyrugundaki istek sayısı :                                                                    " + mainThreadIstek);
                            lock2.lock();
                            mainThreadIstek -= value;
                            lock2.unlock();
                            subThreadIstek += value;

                            if (subThreadIstek > 5000) {
                                value = subThreadIstek - 5000;

                                subThreadIstek = 5000;

                            }

                            System.out.println("subThread1 altsunucu   " + value + " adet isteği subThread1 kuyruğuna ekledi. subThread1 kuyruğunda bekleyen istek toplam " + subThreadIstek + " oldu.");
                            st1say = 1;
                        } else {
                            if (st1say != 0) {
                                subThreadIstek += mainThreadIstek;
                                System.out.println("subThread1 altsunucu   " + mainThreadIstek + " adet isteği subThread1 kuyruğa ekledi. subThread1 kuyrukta bekleyen istek toplam " + subThreadIstek + " oldu.");

                                //System.out.println("Main sunucu  " + istek + " adet isteği kuyruga aldı.");
                                lock2.lock();
                                mainThreadIstek = 0;
                                lock2.unlock();
                                System.out.println("MainThread kuyrugundaki istek sayısı :                                                                   " + mainThreadIstek);

                                st1say = 0;
                            }
                        }
                        lock1.notify();

                    }

                }

            }

        });

        Thread subThread1_istek_cevaplayici = new Thread(new Runnable() {

            @Override
            public void run() {//subThread2 istek yanıt threadi

                while (true) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    synchronized (lock1) {

                        if (subThreadIstek <= 0) {

                            try {
                                lock1.wait();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        //Random random = new Random();

                        Integer value1 = random.nextInt(49) + 1;

                        if (subThreadIstek > value1) {

                            subThreadIstek -= value1;
                            System.out.println("subThread1 altsunucu kurukta bekleyen " + value1 + " adet istek cevaplandı.");
                            System.out.println("subThread1 kurukta bekleyen islem sayısı : " + subThreadIstek);
                            st1say1 = 1;
                            //queue.remove(value1);
                        } else {
                            if (st1say1 != 0) {
                                System.out.println("subThread1 altsunucu kurukta bekleyen " + subThreadIstek + " adet istek cevaplandı.");

                                subThreadIstek = 0;

                                System.out.println("subThread1 kurukta bekleyen islem sayısı : " + subThreadIstek);
                                st1say1 = 0;
                            }
                        }
                        if (subThreadIstek < 5000) {
                            lock1.notify();
                        }

                    }

                }

            }

        });

        /*
         * ****************************************************************sub**************************************************************************
         *
         */
        Thread subThread2_istek_alici = new Thread(new Runnable() {

            @Override
            public void run() {         // mainThreaden İstek alım yükünü hafifletme***************************************************************

                while (true) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    synchronized (lock3) {

                        if (subThreadIstek2 >= 5000) {

                            try {
                                System.out.println("SubThread2 kapasitesi doldu biraz bekleyiniz!");
                                lock3.wait();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }

                        Integer value = random.nextInt(49) + 1;

                        if (value <= mainThreadIstek) {
                            System.out.println("MainThread kuyrugundaki istek sayısı :                                                                    " + mainThreadIstek);
                            lock2.lock();
                            mainThreadIstek -= value;
                            lock2.unlock();
                            subThreadIstek2 += value;

                            if (subThreadIstek2 > 5000) {
                                value = subThreadIstek2 - 5000;

                                subThreadIstek2 = 5000;

                            }

                            System.out.println("subThread2 altsunucu   " + value + " adet isteği subThread2 kuyruğuna ekledi. subThread2 kuyruğunda bekleyen istek toplam " + subThreadIstek2 + " oldu.");
                            st2say = 1;
                        } else {
                            if (st2say != 0) {
                                subThreadIstek2 += mainThreadIstek;
                                System.out.println("subThread2 altsunucu   " + mainThreadIstek + " adet isteği subThread1 kuyruğa ekledi. subThread2 kuyrukta bekleyen istek toplam " + subThreadIstek2 + " oldu.");

                                //System.out.println("Main sunucu  " + istek + " adet isteği kuyruga aldı.");
                                lock2.lock();
                                mainThreadIstek = 0;
                                lock2.unlock();
                                System.out.println("MainThread kuyrugundaki istek sayısı :                                                                   " + mainThreadIstek);

                                st2say = 0;
                            }
                        }
                        lock3.notify();

                    }

                }

            }

        });

        Thread subThread2_istek_cevaplayici = new Thread(new Runnable() {

            @Override
            public void run() {//subThread2 istek yanıt threadi

                while (true) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    synchronized (lock3) {

                        if (subThreadIstek2 <= 0) {

                            try {
                                lock3.wait();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        //Random random = new Random();

                        Integer value1 = random.nextInt(49) + 1;

                        if (subThreadIstek2 > value1) {

                            subThreadIstek2 -= value1;
                            System.out.println("subThread2 altsunucu kurukta bekleyen " + value1 + " adet istek cevaplandı.");
                            System.out.println("subThread2 kurukta bekleyen islem sayısı : " + subThreadIstek2);
                            st2say1 = 1;
                            //queue.remove(value1);
                        } else {
                            if (st2say1 != 0) {
                                System.out.println("subThread2 altsunucu kurukta bekleyen " + subThreadIstek2 + " adet istek cevaplandı.");

                                subThreadIstek2 = 0;

                                System.out.println("subThread2 kurukta bekleyen islem sayısı : " + subThreadIstek2);
                                st2say1 = 0;
                            }
                        }
                        if (subThreadIstek2 < 5000) {
                            lock3.notify();
                        }

                    }

                }

            }

        });

        main_istek_alici.start();
        main_istek_cevaplayici.start();
        //subThread1_istek_alici.start();
        //subThread1_istek_cevaplayici.start();
        //subThread2_istek_alici.start();
        //subThread2_istek_cevaplayici.start();
        try {
            main_istek_alici.join();
            main_istek_cevaplayici.join();
            //subThread1_istek_alici.join();
            //subThread1_istek_cevaplayici.join();
            //subThread2_istek_alici.join();
            //subThread2_istek_cevaplayici.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Yazlab_2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {

        new frm().show();

        new Yazlab_2().mainSunucu();

    }

    public static int getIstek() {
        return istek;
    }

    public static void setIstek(int istek) {
        Yazlab_2.istek = istek;
    }

    public static int getMainThreadIstek() {
        return mainThreadIstek;
    }

    public static void setMainThreadIstek(int mainThreadIstek) {
        Yazlab_2.mainThreadIstek = mainThreadIstek;
    }

    public Lock getLock2() {
        return lock2;
    }

    public void setLock2(Lock lock2) {
        this.lock2 = lock2;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
