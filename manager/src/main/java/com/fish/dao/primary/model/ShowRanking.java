package com.fish.dao.primary.model;

import java.util.Date;

public class ShowRanking {
   private  Ranking ranking;
   private  String  gamesName;
   private  String  formatName;

   public Ranking getRanking() {
      return ranking;
   }

   public void setRanking(Ranking ranking) {
      this.ranking = ranking;
   }

   public String getGamesName() {
      return gamesName;
   }

   public void setGamesName(String gamesName) {
      this.gamesName = gamesName;
   }

   public String getFormatName() {
      return formatName;
   }

   public void setFormatName(String formatName) {
      this.formatName = formatName;
   }
}