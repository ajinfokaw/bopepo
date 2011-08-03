package org.jrimum.texgit.type.component;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.jrimum.utilix.Objects.isNotNull;

import java.text.Format;

import org.jrimum.utilix.Objects;




@SuppressWarnings("serial")
public class FixedField<G> extends Field<G> implements org.jrimum.texgit.type.FixedField<G>{
	
	/**
	 * <p>
	 * Tamanho de especificação e parâmetro da string de leitura ou escrita do campo.
	 * </p>
	 */
	private Integer length;
	
	/**
	 * <p>
	 * Preenchedor do valor utilizado na hora da escrita.
	 * </p>
	 */
	private Filler<?> filler;
	
	
	/**
	 * <p>
	 * Tamanho da string de escrita do campo.
	 * </p>
	 */
	private Integer instantLength;
	
	/**
	 * <p>
	 * Ao ultrapassar o tamanho, define se pode truncar ou se dispara uma exceção.
	 * </p>
	 */
	private boolean truncate;

	
	/**
	 * 
	 */
	public FixedField() {
		super();
	}
	
	/**
	 * @param ordem
	 * @param length
	 * @param filler
	 */
	public FixedField(String name, G value, Integer length, Filler<?> filler) {
		super(name,value);
		setFixedLength(length);
		setFiller(filler);
	}
	
	/**
	 * @param ordem
	 * @param length
	 * @param filler
	 */
	public FixedField(G value, Format formatter, Integer length, Filler<?> filler) {
		super(value,formatter);
		setFixedLength(length);
		setFiller(filler);
	}
	
	/**
	 * @param ordem
	 * @param length
	 * @param filler
	 */
	public FixedField(String name, G value, Format formatter, Integer length, Filler<?> filler) {
		super(name,value,formatter);
		setFixedLength(length);
		setFiller(filler);
	}

	@Override
	public FixedField<G> clone() throws CloneNotSupportedException {
		
		return (FixedField<G>) super.clone();
	}

	/**
	 * @see org.jrimum.texgit.type.component.Field#read(java.lang.String)
	 */
	@Override
	public void read(String str) {

		Objects.checkNotNull(str, "String inválida [null]!");

		if (str.length() == getFixedLength()) {
			super.read(str);
		} else
			throw new IllegalArgumentException(format("Tamanho da string [%s] diferente do especificado [%s]! %s",str.length(),getFixedLength(),toString()));
	}

	/**
	 * @see org.jrimum.texgit.type.component.Field#write()
	 */
	@Override
	public String write() {
		
		String str = fill(super.write());

		instantLength = str.length();
		
		if (isTruncate() && instantLength > getFixedLength()) {
			str = str.substring(0, getFixedLength());
			instantLength = getFixedLength();
		}
		
		isFixedAsDefined();
			
		return str;
	}

	private String fill(String str) {
		
		if(isNotNull(filler))
			str = filler.fill(str, length);
		
		return str;
	}
	
	public boolean isFixedAsDefined() throws IllegalStateException {
		
		if(instantLength.equals(getFixedLength()))
			return true;
		else
			throw new IllegalStateException(format("Tamanho da string [%s] diferente do especificado [%s]! %s",instantLength,getFixedLength(),toString()));
	}
	
	public Integer getFixedLength() {
		
		return this.length;
	}

	public void setFixedLength(Integer length) {
		
		if (isNotNull(length))
			this.length = length;
		else
			throw new IllegalArgumentException(format("Comprimento inválido [%s]!",length));
		
	}
	
	public Filler<?> getFiller() {
		return filler;
	}

	public void setFiller(Filler<?> filler) {
		
		if(isNotNull(filler))
			this.filler = filler;
		else
			throw new IllegalArgumentException(format("Preenchedor inválido [%s]!",filler));
	}

	public boolean isTruncate() {
		return this.truncate;
	}

	public void setTruncate(boolean truncate) {
		this.truncate = truncate;
	}

	
	@Override
	public String toString() {

		return format(
				"%s FixedField [length=%s, instantLength=%s, filler=%s, truncate=%s]",
				super.toString()
				, Objects.whenNull(this.length, EMPTY)
				, Objects.whenNull(this.instantLength, EMPTY)
				, Objects.whenNull(this.filler, EMPTY)
				, Objects.whenNull(this.truncate, EMPTY));
	}

}
