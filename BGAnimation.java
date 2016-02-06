import dxn.Animations;

public class BGAnimation extends Animations
{
    public BGAnimation()
    {
        loadImages("PokemonBattleSimulator/sprites/background/",".png");
        setFrameDelay(100);
        rescale(340,160);
    }
}