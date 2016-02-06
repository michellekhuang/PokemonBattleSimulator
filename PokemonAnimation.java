//Controls idle animations
import dxn.Animations;

public class PokemonAnimation extends Animations
{
    String frontback;
    
    public PokemonAnimation(String pokemonName, String fb)
    {
        frontback = fb;
        loadImages("PokemonBattleSimulator/sprites/pokemon/" + pokemonName + "/IdleAnimation/" + frontback + "/", ".png");
        setFrameDelay(85);
    }
    
    public void setPokemonToolTip(Pokemon poke)
    {
        setToolTipText("<html>" + poke.getName()
                                                + "<br>" + String.format("%.1f", poke.getCurrentHP())
                                                + "/" + poke.getHP() + " HP"
                                                + "<br>" + poke.getType1()
                                                + "/" + poke.getType2());
    }
}