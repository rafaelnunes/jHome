package br.com.globalcode.jhome.twitter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import br.com.globalcode.jhome.DeviceManager;
import br.com.globalcode.jhome.Environment;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

/**
 *
 * @author vsenger
 */
@Singleton
@Startup
public class TwitterUpdaterBean {

  final Object LOCK = new Object();
  HashSet tweetList = new HashSet<Tweet>();
  Twitter twitter = null;
  User user = null;
  @EJB
  private DeviceManager deviceManager;
  @EJB
  private Environment environment;
  @Resource
  TimerService timerService;
  // Add business logic below. (Right-click in editor and choose
  // "Insert Code > Add Business Method")

  @PostConstruct
  public void init() {
    Logger.getLogger(TwitterUpdaterBean.class.getName()).log(Level.INFO, "Starting jHome Twitter Module");

    twitter = new TwitterFactory().getInstance();
    try {
      user = twitter.verifyCredentials();

      startUpdater(5000);
    } catch (TwitterException ex) {
      Logger.getLogger(TwitterUpdaterBean.class.getName()).log(Level.SEVERE,
              "Error connecting to Twitter:" + ex.getMessage(), ex);
    }
  }

  public void initAsync() {
    System.out.println("configurando listener..");

    AsyncTwitterFactory factory = new AsyncTwitterFactory();
    AsyncTwitter twitter = factory.getInstance();
    twitter.addListener(new TwitterAdapter() {

      @Override
      public void gotMentions(ResponseList statuses) {
        System.out.println("chamou....");

        for (Object o : statuses) {
          Status status = (Status) o;
          Tweet tweet = new Tweet(status.getCreatedAt(),
                  status.getUser().getScreenName(),
                  status.getText());
          if (check(tweet)) {
            Logger.getLogger(TwitterUpdaterBean.class.getName()).log(Level.INFO,
                    "New tweet: " + status.getCreatedAt() + " - "
                    + status.getUser().getScreenName() + status.getText());
            synchronized (LOCK) {
              LOCK.notify();
            }
          }
        }
      }
    });
    synchronized (LOCK) {
      try {
        LOCK.wait();
      } catch (InterruptedException ex) {
        Logger.getLogger(TwitterUpdaterBean.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @PreDestroy
  public void stopTimer() {
    Collection<Timer> timers = timerService.getTimers();
    for (Timer t : timers) {
      t.cancel();
    }
  }

  public boolean check(Tweet tweet) {
    if (tweetList.contains(tweet)) {
      return false;
    } else {
      tweetList.add(tweet);
      return true;
    }
  }

  @Timeout
  private void execute(Timer timer) {
    List<Status> statuses;
    try {
      statuses = twitter.getMentions();
    } catch (TwitterException ex) {
      Logger.getLogger(TwitterUpdaterBean.class.getName()).log(Level.SEVERE, "Error reading twitter mentions: " + ex.getMessage(), ex);
      return;
    }
    Logger.getLogger(TwitterUpdaterBean.class.getName()).log(Level.INFO, "Checking Twitter Messages");

    for (Status status : statuses) {
      Tweet tweet = new Tweet(status.getCreatedAt(),
              status.getUser().getScreenName(),
              status.getText());
      if (check(tweet)) {
        Logger.getLogger(TwitterUpdaterBean.class.getName()).log(Level.INFO,
                "New tweet: " + status.getCreatedAt() + " - "
                + status.getUser().getScreenName() + status.getText());
        try {
          if (tweet.getText().toLowerCase().indexOf("#tdc2011") == 0) {
            return;
          }
          if (tweet.getText().toLowerCase().indexOf("desligar lampada") != -1) {
            deviceManager.execute("lamp", "-");
          } else if (tweet.getText().toLowerCase().indexOf("ligar lampada") != -1) {
            deviceManager.execute("lamp", "+");
          } else if (tweet.getText().toLowerCase().indexOf("frente") != -1) {
            deviceManager.execute("frente");
          } else if (tweet.getText().toLowerCase().indexOf("tras") != -1) {
            deviceManager.execute("tras");
          } else if (tweet.getText().toLowerCase().indexOf("esquerda") != -1) {
            deviceManager.execute("esquerda");
          } else if (tweet.getText().toLowerCase().indexOf("direita") != -1) {
            deviceManager.execute("direita");
          } else if (tweet.getText().toLowerCase().indexOf("desligar vermelho") != -1) {
            deviceManager.execute("RGB", "R9");
          } else if (tweet.getText().toLowerCase().indexOf("desligar verde") != -1) {
            deviceManager.execute("RGB", "G9");
          } else if (tweet.getText().toLowerCase().indexOf("desligar azul") != -1) {
            deviceManager.execute("RGB", "B9");
          } else if (tweet.getText().toLowerCase().indexOf("ligar vermelho") != -1) {
            deviceManager.execute("RGB", "R0");
          } else if (tweet.getText().toLowerCase().indexOf("ligar verde") != -1) {
            deviceManager.execute("RGB", "G0");
          } else if (tweet.getText().toLowerCase().indexOf("ligar azul") != -1) {
            deviceManager.execute("RGB", "B0");
          }
        } catch (Exception ex) {
          Logger.getLogger(TwitterUpdaterBean.class.getName()).log(Level.SEVERE, "Error executing jHome command " + ex.getMessage(), ex);
        }
      }
    }
  }

  public void startUpdater(long interval) {
    Logger.getLogger(TwitterUpdaterBean.class.getName()).log(Level.INFO,
            "Initializing TwitterUpdater. I'm going to check each " + interval / 1000 + "seconds");
    
    timerService.createTimer(
            0, interval, "");
  }
}
