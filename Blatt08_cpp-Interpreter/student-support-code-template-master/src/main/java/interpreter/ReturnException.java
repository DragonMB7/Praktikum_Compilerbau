package interpreter;

// Cette exception sert uniquement à transporter la valeur de retour
// hors de la pile d'appels. Ce n'est pas une erreur.
public class ReturnException extends RuntimeException {
  public final Object value;

  public ReturnException(Object value) {
    super(null, null, false, false); // Optimisation (pas de stacktrace)
    this.value = value;
  }
}
