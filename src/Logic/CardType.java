package Logic;

public enum CardType {

    ACEHEART("ah"), ACEDIAMOND("ad"), ACESPADE("as"), ACECLUB("ac"),
    TWOHEART("2h"), TWODIAMOND("2d"), TWOSPADE("2s"), TWOCLUB("2c"),
    THREEHEART("3h"), THREEDIAMOND("3d"), THREESPADE("3s"), THREECLUB("3c"),
    FOURHEART("4h"), FOURDIAMOND("4d"), FOURSPADE("4s"), FOURCLUB("4c"),
    FIVEHEART("5h"), FIVEDIAMOND("5d"), FIVESPADE("5s"), FIVECLUB("5c"),
    SIXHEART("6h"), SIXDIAMOND("6d"), SIXSPADE("6s"), SIXCLUB("6c"),
    SEVENHEART("7h"), SEVENDIAMOND("7d"), SEVENSPADE("7s"), SEVENCLUB("7c"),
    EIGHTHEART("8h"), EIGHTDIAMOND("8d"), EIGHTSPADE("8s"), EIGHTCLUB("8c"),
    NINEHEART("9h"), NINEDIAMOND("9d"), NINESPADE("9s"), NINECLUB("9c"),
    TENHEART("10h"), TENDIAMOND("10d"), TENSPADE("10s"), TENCLUB("10c"),
    JACKHEART("jh"), JACKDIAMOND("jd"), JACKSPADE("js"), JACKCLUB("jc"),
    QUEENHEART("qh"), QUEENDIAMOND("qd"), QUEENSPADE("qs"), QUEENCLUB("qc"),
    KINGHEART("kh"), KINGDIAMOND("kd"), KINGSPADE("ks"), KINGCLUB("kc"),
    BACK("back"), JOKER("joker");

    public final String path;

    CardType(String path) {
        this.path = path;
    }
}
