/**
 * @author Alexander Rüedlinger
 */
package models.visitor;

public interface Visitable {
	void accept(Visitor visitor);
}
